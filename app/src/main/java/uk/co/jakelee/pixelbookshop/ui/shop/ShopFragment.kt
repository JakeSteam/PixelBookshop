package uk.co.jakelee.pixelbookshop.ui.shop

import android.R
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_shop.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Furniture
import uk.co.jakelee.pixelbookshop.ui.shop.ShopViewModel.MyResult




class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(uk.co.jakelee.pixelbookshop.R.layout.fragment_shop, container, false)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        return root
    }

    override fun onResume() {
        super.onResume()

        shopViewModel.usersBooksLiveDataMerger().observe(viewLifecycleOwner, Observer<MyResult> { result ->
            if (result.isComplete) return@Observer
            val maxX = result.floors!!.last().x
            val maxY = result.floors!!.first().y
            drawFloors(result.floors!!, maxX)
            drawWalls(result.floors!!, result.wall!!, maxX, maxY)
            drawFurni(result.furnitures!!, maxX)
        })
    }

    private fun drawFurni(furnitures: List<OwnedFurnitureWithOwnedBooks>, maxX: Int) {
        furniture_layer.removeAllViews()
        furnitures.forEach {
            val books = it.ownedBooks
            val furniture = it.ownedFurniture
            val resource =
                getResource(furniture.furniture, furniture.isFacingEast, books.isNotEmpty())
            val callback = { tile: OwnedFurniture ->
                shopViewModel.upgradeFurni(tile)
                Unit
            }
            furniture_layer.addView(
                createTile(furniture, resource, callback),
                getTileParams(it.ownedFurniture.x, it.ownedFurniture.y, maxX)
            )
        }
    }

    private fun drawFloors(floors: List<OwnedFloor>, maxX: Int) {
        floor_layer.removeAllViews()
        floors.forEach {
            val floorResource = it.floor?.let { floor ->
                if (it.isFacingEast) floor.imageEast else floor.imageNorth
            } ?: R.color.transparent
            val floorCallback = { tile: Tile ->
                shopViewModel.upgradeFloor(tile as OwnedFloor)
                Unit
            }
            val params = getTileParams(it.x, it.y, maxX)
            floor_layer.addView(createTile(it, floorResource, floorCallback), params)
        }
    }

    private fun drawWalls(floors: List<OwnedFloor>, wall: WallInfo, maxX: Int, maxY: Int) {
        wall_layer.removeAllViews()
        val wallCallback = { fullWall: WallInfo ->
            shopViewModel.upgradeWall(fullWall.wall, 1)
            Unit
        }
        floors.forEach {
            val params = getTileParams(it.x, it.y, maxX)
            if (it.x == 0 || it.y == maxY) {
                getWallAsset(it.x, it.y, wall, maxY)?.let { image ->
                    wall_layer.addView(
                        createTile(wall, image, wallCallback),
                        params
                    )
                }
            }
        }
    }

    private fun getWallAsset(x: Int, y: Int, wall: WallInfo, maxY: Int): Int? {
        return if (isDoor(wall, x, y)) {
            wall.wall.imageDoor
        } else if (x == 0 && y == maxY) {
            wall.wall.imageCorner
        } else if (x == 0) {
            wall.wall.imageEast
        } else if (y == maxY) {
            wall.wall.imageNorth
        } else {
            null
        }
    }

    private fun isDoor(wall: WallInfo, x: Int, y: Int) =
        wall.isDoorOnX && wall.doorPosition == x || !wall.isDoorOnX && wall.doorPosition == y

    private fun getResource(furniture: Furniture, isFacingEast: Boolean, hasBooks: Boolean) = when {
        hasBooks && isFacingEast -> furniture.imageEastFilled ?: furniture.imageEast
        hasBooks && !isFacingEast -> furniture.imageNorthFilled ?: furniture.imageNorth
        !hasBooks && isFacingEast -> furniture.imageEast
        else -> furniture.imageNorth
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun <T : Any> createTile(tile: T, @DrawableRes resource: Int, callback: (T) -> (Unit)) =
        ImageView(activity!!).apply {
            isDrawingCacheEnabled = true
            setImageResource(resource)
            setOnTouchListener(View.OnTouchListener { v, event ->
                val bmp = Bitmap.createBitmap(v.drawingCache)
                if (event.x.toInt() > bmp.width || event.y.toInt() > bmp.height) return@OnTouchListener true
                val color = bmp.getPixel(event.x.toInt(), event.y.toInt())
                if (color == Color.TRANSPARENT) {
                    return@OnTouchListener false
                } else {
                    if (event.action == MotionEvent.ACTION_UP) {
                        callback.invoke(tile)
                    }
                    return@OnTouchListener true
                }
            })
        }

    private fun getTileParams(x: Int, y: Int, xTiles: Int): RelativeLayout.LayoutParams {
        val tileWidth = 64
        val tileHeight = 128
        val layoutParams = RelativeLayout.LayoutParams(tileWidth, tileHeight)
        val leftPadding = (x + y) * (tileWidth / 2)
        val topPadding = (xTiles + (x - y)) * (tileHeight / 8)
        layoutParams.setMargins(leftPadding, topPadding, 0, 0)
        return layoutParams
    }

}