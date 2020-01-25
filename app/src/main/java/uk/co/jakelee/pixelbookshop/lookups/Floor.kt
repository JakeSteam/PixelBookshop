package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Floor (override val id: Int, tier: Int, @StringRes name: Int, @DrawableRes val image: Int, cost: Int, modifier: Double): Model {
    Dirt(1, 1, R.string.floor_dirt, R.drawable.floor_dirt, 10, 1.0),
    Wood(2, 2, R.string.floor_wood, R.drawable.floor_planks, 100, 1.1),
    Marble(3, 3, R.string.floor_marble, R.drawable.floor_marble, 1000, 1.2)
}