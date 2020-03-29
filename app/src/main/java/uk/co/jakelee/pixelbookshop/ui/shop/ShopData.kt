package uk.co.jakelee.pixelbookshop.ui.shop

import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo

class ShopData {
    var wall: WallInfo? = null
    var floors: List<OwnedFloor>? = null
    var furnitures: List<OwnedFurnitureWithOwnedBooks>? = null

    fun isValid() = wall != null
            && floors?.size ?: 0 > 0
            && furnitures?.size ?: 0 > 0
}