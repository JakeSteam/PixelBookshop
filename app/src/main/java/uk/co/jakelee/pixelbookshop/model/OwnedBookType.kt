package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class OwnedBookType(val id: Int, @StringRes name: Int, frequency: Double, modifier: Double) {
    FirstEdition(1, R.string.type_firstedition, 0.005, 20.0),
    SignedEdition(2, R.string.type_signed, 0.01, 4.0),
    Hardback(3, R.string.type_hardback, 0.7, 1.5),
    Paperback(4, R.string.type_paperback, 1.0, 1.0),
    ReviewCopy(5, R.string.type_reviewcopy, 0.2, 1.0)
}