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
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_shop.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.model.Furniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.interfaces.Tile


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

        // Y = left to top
        // X = left to bottom

        val floors = getFloor() // Sorted ascending y, then descending x,
        val maxX = floors.first().first().x
        floor_layer.removeAllViews()
        floors.forEach { floor ->
            floor.forEach {
                val resource = if (it.exists) R.drawable.floor_planks else android.R.color.transparent
                val callback = { tile: Tile -> Toast.makeText(activity, "Clicked tile (${tile.x},${tile.y})!", Toast.LENGTH_SHORT).show() }
                floor_layer.addView(
                    createTile(it, resource, callback),
                    getTileParams(it.x, it.y, maxX)
                )
            }
        }

        val furnitures = getFurniture()
        furniture_layer.removeAllViews()
        furnitures.forEach { row ->
            row.forEach {
                val resource = if (it.furnitureId == Furniture.SmallCrate.id) R.drawable.furniture_crate else android.R.color.transparent
                val callback = { tile: Tile -> Toast.makeText(activity, "Clicked furniture (${tile.x},${tile.y})!", Toast.LENGTH_SHORT).show() }
                floor_layer.addView(
                    createTile(it, resource, callback),
                    getTileParams(it.x, it.y, maxX)
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createTile(tile: Tile, @DrawableRes resource: Int, callback: (Tile) -> (Unit)) =
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
        val topPadding = (xTiles + (y - x)) * (tileHeight / 8)
        layoutParams.setMargins(leftPadding, topPadding, 0, 0)
        return layoutParams
    }

    private fun getFurniture(): List<List<OwnedFurniture>> {
        return listOf(
            listOf(
                OwnedFurniture(3, 0, true, Furniture.SmallCrate.id),
                OwnedFurniture(2, 0, true, Furniture.SmallCrate.id),
                OwnedFurniture(1, 0, true, Furniture.SmallCrate.id),
                OwnedFurniture(0, 0, true, Furniture.SmallCrate.id)
            ),
            listOf(
                OwnedFurniture(3, 5, false, Furniture.SmallCrate.id),
                OwnedFurniture(3, 6, false, Furniture.BigCrate.id)
            )
        )
    }

    // List[Y][X]
    private fun getFloor(): List<List<OwnedFloor>> {
        return listOf(
            listOf(
                OwnedFloor(3, 0, true),
                OwnedFloor(2, 0, true),
                OwnedFloor(1, 0, true),
                OwnedFloor(0, 0, true)
            ), listOf(
                OwnedFloor(3, 1, true),
                OwnedFloor(2, 1, false),
                OwnedFloor(1, 1, true),
                OwnedFloor(0, 1, false)
            ), listOf(
                OwnedFloor(3, 2, false),
                OwnedFloor(2, 2, false),
                OwnedFloor(1, 2, true),
                OwnedFloor(0, 2, false)
            ), listOf(
                OwnedFloor(3, 3, true),
                OwnedFloor(2, 3, false),
                OwnedFloor(1, 3, true),
                OwnedFloor(0, 3, true)
            ), listOf(
                OwnedFloor(3, 4, true),
                OwnedFloor(2, 4, true),
                OwnedFloor(1, 4, true),
                OwnedFloor(0, 4, true)
            ), listOf(
                OwnedFloor(3, 5, true),
                OwnedFloor(2, 5, true),
                OwnedFloor(1, 5, true),
                OwnedFloor(0, 5, true)
            ), listOf(
                OwnedFloor(3, 6, true),
                OwnedFloor(2, 6, true),
                OwnedFloor(1, 6, true),
                OwnedFloor(0, 6, true)
            )
        )
    }

}