package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class Floor (val id: Int, tier: Int, @StringRes name: Int, cost: Int, modifier: Double) {
    Dirt(1, 1, R.string.floor_dirt, 10, 1.0),
    Wood(2, 2, R.string.floor_wood, 100, 1.1),
    Marble(3, 3, R.string.floor_marble, 1000, 1.2),
}