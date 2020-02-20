package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class FurnitureType(override val id: Int, @StringRes val title: Int): Model {
    Decoration(1, R.string.furniture_decoration),
    Display(2, R.string.furniture_display),
    Seating(3, R.string.furniture_seating),
    Storage(4, R.string.furniture_storage)
}