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
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Furniture


class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(R.layout.fragment_shop, container, false)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        return root
    }

    override fun onResume() {
        super.onResume()

        shopViewModel.wall.observe(viewLifecycleOwner, Observer { _ -> })
        shopViewModel.ownedFloor.observe(viewLifecycleOwner, Observer { floors ->
            if (floors.isNotEmpty()) {
                val maxX = floors.last().x
                floor_layer.removeAllViews()
                wall_layer.removeAllViews()
                floors.forEach {
                    val floorResource = it.floor?.let { floor ->
                        if (it.isFacingEast) floor.imageEast else floor.imageNorth
                    } ?: android.R.color.transparent
                    val floorCallback = { tile: Tile ->
                        shopViewModel.upgradeFloor(tile as OwnedFloor)
                        Unit
                    }
                    val params = getTileParams(it.x, it.y, maxX)
                    floor_layer.addView(createTile(it, floorResource, floorCallback), params)

                    if ((it.x == 0 || it.y == 5) && shopViewModel.wall.value != null) {
                        val wall = shopViewModel.wall.value!!
                        val wallCallback = { fullWall: WallInfo ->
                            shopViewModel.upgradeWall(fullWall.wall, 1)
                            Unit
                        }
                        if (wall.isDoorOnX && wall.doorPosition == it.x
                            || !wall.isDoorOnX && wall.doorPosition == it.y) {
                            wall_layer.addView(
                                createTile(wall, wall.wall.imageDoor, wallCallback),
                                params
                            )
                        } else if (it.x == 0 && it.y == 5) {
                            wall_layer.addView(
                                createTile(wall, wall.wall.imageCorner, wallCallback),
                                params
                            )
                        } else if (it.x == 0) {
                            wall_layer.addView(
                                createTile(wall, wall.wall.imageEast, wallCallback),
                                params
                            )
                        } else if (it.y == 5) {
                            wall_layer.addView(
                                createTile(wall, wall.wall.imageNorth, wallCallback),
                                params
                            )
                        }
                    }
                }
            }
        })

        shopViewModel.ownedFurniture.observe(viewLifecycleOwner, Observer { furnitures ->
            if (furnitures.isNotEmpty()) {
                furniture_layer.removeAllViews()
                furnitures.forEach {
                    val books = it.ownedBooks
                    val furniture = it.ownedFurniture
                    val resource = getResource(furniture.furniture, furniture.isFacingEast, books.isNotEmpty())
                    val callback = { _: Tile -> }
                    furniture_layer.addView(
                        createTile(furniture, resource, callback),
                        getTileParams(it.ownedFurniture.x, it.ownedFurniture.y, 7)
                    )
                }
            }
        })
    }

    private fun getResource(furniture: Furniture, isFacingEast: Boolean, hasBooks: Boolean): Int {
        return if (hasBooks && isFacingEast) {
            furniture.imageEastFilled ?: furniture.imageEast
        } else if (hasBooks && !isFacingEast) {
            furniture.imageNorthFilled ?: furniture.imageNorth
        } else if (!hasBooks && isFacingEast) {
            furniture.imageEast
        } else {
            furniture.imageNorth
        }
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