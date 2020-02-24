package uk.co.jakelee.pixelbookshop.ui.stock

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class FullBookData {
    var books: List<OwnedBook>? = null
    var sortField: Pair<Int, Int>? = null
    var filterField: Pair<Int, Int>? = null

    fun isValid() = books != null
            && sortField != null
            && filterField != null
}