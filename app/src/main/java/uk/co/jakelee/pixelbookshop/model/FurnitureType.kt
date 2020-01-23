package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class FurnitureType(id: Int, @StringRes name: Int) {
    Decoration(1, R.string.furniture_decoration),
    Display(2, R.string.furniture_display),
    Seating(3, R.string.furniture_seating),
    Storage(4, R.string.furniture_storage)
}