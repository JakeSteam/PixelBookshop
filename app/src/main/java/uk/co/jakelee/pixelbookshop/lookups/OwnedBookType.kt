package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class OwnedBookType(override val id: Int, @StringRes name: Int, frequency: Double, modifier: Double): Model {
    FirstEdition(1, R.string.type_firstedition, 0.005, 20.0),
    SignedEdition(2, R.string.type_signed, 0.01, 4.0),
    Hardback(3, R.string.type_hardback, 0.7, 1.5),
    Paperback(4, R.string.type_paperback, 1.0, 1.0),
    ReviewCopy(5, R.string.type_reviewcopy, 0.2, 1.0)
}