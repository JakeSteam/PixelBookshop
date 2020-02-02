package uk.co.jakelee.pixelbookshop.ui.shop

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
import kotlinx.android.synthetic.main.fragment_shop.view.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Furniture

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    enum class SelectedTab { NONE, ROTATE, MOVE, UPGRADE }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(uk.co.jakelee.pixelbookshop.R.layout.fragment_shop, container, false)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        root.button_show.setOnClickListener { showControls() }
        root.button_hide.setOnClickListener { hideControls() }
        root.button_rotate.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.ROTATE) }
        root.button_upgrade.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.UPGRADE) }
        root.button_move.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.MOVE) }
        shopViewModel.getShopData().observe(viewLifecycleOwner, Observer<ShopData> { result ->
            if (result.wall == null || result.floors?.size ?: 0 == 0 || result.furnitures?.size ?: 0 == 0) return@Observer
            val maxX = result.floors!!.last().x
            val maxY = result.floors!!.first().y
            drawFloors(result.floors!!, maxX)
            drawWalls(result.floors!!, result.wall!!, maxX, maxY)
            drawFurniture(result.furnitures!!, maxX)
        })
        shopViewModel.currentTab.observe(viewLifecycleOwner, Observer<SelectedTab> {
            updateCurrentTabButtons()
            when (it) {
                SelectedTab.ROTATE -> { setSelectedLayers(true, false, true) }
                SelectedTab.MOVE-> { setSelectedLayers(false, false, true) }
                SelectedTab.UPGRADE -> { setSelectedLayers(true, true, true) }
                else -> { setSelectedLayers(true, true, true) }
            }
        })
        return root
    }

    private fun updateCurrentTabButtons() {
        shopViewModel.currentTab.value?.let {
            val default = it == SelectedTab.NONE
            button_rotate.alpha = if (default || it == SelectedTab.ROTATE) 1.0f else 0.5f
            button_upgrade.alpha = if (default || it == SelectedTab.UPGRADE) 1.0f else 0.5f
            button_move.alpha = if (default || it == SelectedTab.MOVE) 1.0f else 0.5f
        }
    }

    private fun setSelectedLayers(floor: Boolean, wall: Boolean, furniture: Boolean) {
        floor_layer.alpha = if (floor) 1.0f else 0.5f
        wall_layer.alpha = if (wall) 1.0f else 0.5f
        furniture_layer.alpha = if (furniture) 1.0f else 0.5f
    }

    private fun showControls() {
        controls.visibility = View.VISIBLE
        button_show.visibility = View.GONE
    }

    private fun hideControls() {
        controls.visibility = View.GONE
        button_show.visibility = View.VISIBLE
        shopViewModel.setOrResetMode(SelectedTab.NONE)
    }

    private fun drawFloors(floors: List<OwnedFloor>, maxX: Int) {
        floor_layer.removeAllViews()
        floors.forEach {
            val floorResource = it.floor?.let { floor ->
                if (it.isFacingEast) floor.imageEast else floor.imageNorth
            } ?: android.R.color.transparent
            val floorCallback = { floor: OwnedFloor -> shopViewModel.floorClick(floor) }
            val params = getTileParams(it.x, it.y, maxX, false)
            floor_layer.addView(createTile(it, floorResource, floorCallback), params)
        }
    }

    private fun drawWalls(floors: List<OwnedFloor>, wallInfo: WallInfo, maxX: Int, maxY: Int) {
        wall_layer.removeAllViews()
        val wallCallback = { wall: WallInfo -> shopViewModel.wallClick(wall, 1) }
        floors.forEach {
            if (it.x == 0 || it.y == maxY) {
                getWallAsset(it.x, it.y, wallInfo, maxY)?.let { image ->
                    wall_layer.addView(
                        createTile(wallInfo, image, wallCallback),
                        getTileParams(it.x, it.y, maxX, true)
                    )
                }
            }
        }
    }

    private fun drawFurniture(furnitures: List<OwnedFurnitureWithOwnedBooks>, maxX: Int) {
        furniture_layer.removeAllViews()
        furnitures.forEach {
            val books = it.ownedBooks
            val furniture = it.ownedFurniture
            val resource = getResource(furniture.furniture, furniture.isFacingEast, books.isNotEmpty())
            val callback = { tile: OwnedFurniture -> shopViewModel.furniClick(tile) }
            furniture_layer.addView(
                createTile(furniture, resource, callback),
                getTileParams(it.ownedFurniture.x, it.ownedFurniture.y, maxX, true)
            )
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
    private fun <T : Any> createTile(tile: T, @DrawableRes resource: Int, callback: (T) -> (Any)) =
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

    private fun getTileParams(x: Int, y: Int, xTiles: Int, boostVertically: Boolean): RelativeLayout.LayoutParams {
        val tileWidth = 64
        val tileHeight = 128
        val layoutParams = RelativeLayout.LayoutParams(tileWidth, tileHeight)
        val leftPadding = (x + y) * (tileWidth / 2)
        val topOffset = if (boostVertically) 5 else 0
        val topPadding = (xTiles + (x - y)) * (tileHeight / 8) - topOffset
        layoutParams.setMargins(leftPadding, topPadding, 0, 0)
        return layoutParams
    }

}