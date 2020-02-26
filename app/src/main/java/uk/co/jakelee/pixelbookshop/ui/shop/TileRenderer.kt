package uk.co.jakelee.pixelbookshop.ui.shop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Furniture

class TileRenderer(private val context: Context) {
    private val tileWidth = 64
    private val tileHeight = 128

    fun drawFloor(layout: ViewGroup, maxX: Int, ownedFloor: OwnedFloor, callback: (View, OwnedFloor) -> (Any)) {
        val floorResource = ownedFloor.floor?.let { floor ->
            if (ownedFloor.isFacingEast) floor.imageEast else floor.imageNorth
        } ?: android.R.color.transparent
        val tile = createTile(ownedFloor, floorResource, callback)
        val params = getTileParams(ownedFloor.x, ownedFloor.y, maxX, false)
        layout.addView(tile, params)
    }

    fun drawWall(layout: ViewGroup, x: Int, y: Int, maxX: Int, maxY: Int, wallInfo: WallInfo, callback: (View, WallInfo) -> (Any)) {
        wallInfo.getAsset(x, y, maxY)?.let {
            val tile = createTile(wallInfo, it, callback)
            val params = getTileParams(x, y, maxX, true)
            layout.addView(tile, params)
        }
    }

    fun drawFurniture(layout: ViewGroup, ownedFurniture: OwnedFurnitureWithOwnedBooks, maxX: Int, callback: (View, OwnedFurniture) -> Any) {
        val books = ownedFurniture.ownedBooks
        val furniture = ownedFurniture.ownedFurniture
        val resource = getFurnitureResource(furniture.furniture, furniture.isFacingEast, books.isNotEmpty())
        val tile = createTile(furniture, resource, callback)
        val params = getTileParams(furniture.x, furniture.y, maxX, true)
        layout.addView(tile, params)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun <T : Any> createTile(tile: T, @DrawableRes resource: Int, callback: (View, T) -> (Any)) =
        ImageView(context).apply {
            isDrawingCacheEnabled = true
            setImageResource(resource)
            setOnTouchListener(View.OnTouchListener { v, event ->
                val bmp = Bitmap.createBitmap(v.drawingCache)
                if (event.x.toInt() >= bmp.width || event.y.toInt() >= bmp.height) return@OnTouchListener true
                val color = bmp.getPixel(event.x.toInt(), event.y.toInt())
                if (color == Color.TRANSPARENT) {
                    return@OnTouchListener false
                } else {
                    if (event.action == MotionEvent.ACTION_UP) {
                        callback.invoke(this, tile)
                    }
                    return@OnTouchListener true
                }
            })
        }

    private fun getTileParams(x: Int, y: Int, xTiles: Int, boostVertically: Boolean): RelativeLayout.LayoutParams {
        val leftPadding = (x + y) * (tileWidth / 2)
        val topOffset = if (boostVertically) 5 else 0
        val topPadding = (xTiles + (x - y)) * (tileHeight / 8) - topOffset
        val layoutParams = RelativeLayout.LayoutParams(tileWidth, tileHeight)
        layoutParams.setMargins(leftPadding, topPadding, 0, 0)
        return layoutParams
    }

    private fun getFurnitureResource(furniture: Furniture, isFacingEast: Boolean, hasBooks: Boolean) = when {
        hasBooks && isFacingEast -> furniture.imageEastFilled ?: furniture.imageEast
        hasBooks && !isFacingEast -> furniture.imageNorthFilled ?: furniture.imageNorth
        !hasBooks && isFacingEast -> furniture.imageEast
        else -> furniture.imageNorth
    }
}