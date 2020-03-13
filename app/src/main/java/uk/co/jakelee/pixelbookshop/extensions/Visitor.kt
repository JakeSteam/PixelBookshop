package uk.co.jakelee.pixelbookshop.extensions

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.lookups.Visitor
import uk.co.jakelee.pixelbookshop.ui.shop.ShopViewModel
import java.math.BigDecimal

data class VisitorSatisfaction(val satisfaction: Int, val bookValue: BigDecimal)

fun Visitor.getSatisfaction(book: OwnedBook): VisitorSatisfaction {
    var bookValue = book.defaultValue()
    var satisfaction = 0
    if (bookPreference.first == book.book) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookPreference.second))
    }
    if (bookGenrePreference.first == book.book.genre) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookGenrePreference.second))
    }
    if (bookRarityPreference.first == book.book.rarity) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookRarityPreference.second))
    }
    if (bookDefectPreference.first == book.bookDefect) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookDefectPreference.second))
    }
    if (bookSourcePreference.first == book.bookSource) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookSourcePreference.second))
    }
    if (bookTypePreference.first == book.bookType) {
        satisfaction++
        bookValue = bookValue.times(BigDecimal(bookTypePreference.second))
    }
    return VisitorSatisfaction(satisfaction, bookValue)
}