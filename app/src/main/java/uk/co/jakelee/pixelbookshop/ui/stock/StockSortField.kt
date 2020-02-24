package uk.co.jakelee.pixelbookshop.ui.stock

import uk.co.jakelee.pixelbookshop.R

enum class StockSortField(val resource: Int) {
    TITLE(R.string.book_title),
    AUTHOR(R.string.book_author),
    GENRE(R.string.book_genre),
    RARITY(R.string.book_rarity),
    PUBLISHED(R.string.book_published),
    BOOKTYPE(R.string.book_type),
    BOOKSOURCE(R.string.book_source),
    BOOKQUALITY(R.string.book_quality),
    BOOKDEFECT(R.string.book_defect),
    FURNITUREID(R.string.book_furniture),
}