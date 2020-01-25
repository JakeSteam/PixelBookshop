package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Wall (override val id: Int, tier: Int, @StringRes name: Int, @DrawableRes image: Int, cost: Int, modifier: Double): Model {
    Fence(1, 1, R.string.wall_fence, R.drawable.wall_fence, 10, 1.0),
    Brick(2, 2, R.string.wall_brick, R.drawable.wall_brick, 100, 1.1),
    Marble(3, 3, R.string.wall_marble, R.drawable.wall_marble, 1000, 1.2),
}