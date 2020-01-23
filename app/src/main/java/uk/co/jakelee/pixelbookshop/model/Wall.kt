package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class Wall (val id: Int, tier: Int, @StringRes name: Int, @DrawableRes image: Int, cost: Int, modifier: Double) {
    Fence(1, 1, R.string.wall_fence, R.drawable.floor_planks, 10, 1.0),
    Brick(2, 2, R.string.wall_brick, R.drawable.floor_planks, 100, 1.1),
    Marble(3, 3, R.string.wall_marble, R.drawable.floor_planks, 1000, 1.2),
}