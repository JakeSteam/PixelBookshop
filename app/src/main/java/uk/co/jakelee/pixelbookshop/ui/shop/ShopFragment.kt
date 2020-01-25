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
import uk.co.jakelee.pixelbookshop.interfaces.Tile


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

        shopViewModel.ownedFloor.observe(viewLifecycleOwner, Observer { floors ->
            if (floors.isNotEmpty()) {
                val maxX = floors.last().x
                floor_layer.removeAllViews()
                floors.forEach {
                    val resource =
                        if (it.floor?.image != null) it.floor!!.image else android.R.color.transparent
                    val callback = { tile: Tile ->
                        shopViewModel.invertFloor(tile as OwnedFloor)
                        Unit
                    }
                    floor_layer.addView(
                        createTile(it, resource, callback),
                        getTileParams(it.x, it.y, maxX)
                    )
                }
            }
        })

        shopViewModel.ownedFurniture.observe(viewLifecycleOwner, Observer { furnitures ->
            if (furnitures.isNotEmpty()) {
                furniture_layer.removeAllViews()
                furnitures.forEach {
                    val resource = it.furniture.image
                    val callback = { tile: Tile -> }
                    furniture_layer.addView(
                        createTile(it, resource, callback),
                        getTileParams(it.x, it.y, 3)
                    )
                }
            }
        })
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
        val topPadding = (xTiles + (x - y)) * (tileHeight / 8)
        layoutParams.setMargins(leftPadding, topPadding, 0, 0)
        return layoutParams
    }

}