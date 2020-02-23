package uk.co.jakelee.pixelbookshop.ui.stock

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class FullBookData {
    var books: List<OwnedBook>? = null
    var sortFieldIndex: Int? = null
    var sortOrderIndex: Int? = null

    fun isValid() = books != null
            && sortFieldIndex != null
            && sortOrderIndex != null
}