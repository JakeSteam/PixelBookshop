package uk.co.jakelee.pixelbookshop.lookups

import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class NotificationType(override val id: Int): Model {
    Negative(0),
    Neutral(1),
    Positive(2)
}