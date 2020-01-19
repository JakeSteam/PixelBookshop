package uk.co.jakelee.pixelbookshop.data

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class Wall (val id: Int, tier: Int, @StringRes name: Int, cost: Int, modifier: Double) {
    Fence(1, 1, R.string.wall_fence, 10, 1.0),
    Brick(2, 2, R.string.wall_brick, 100, 1.1),
    Marble(3, 3, R.string.wall_marble, 1000, 1.2),
}