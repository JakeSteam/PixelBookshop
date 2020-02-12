package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Wall(
    override val id: Int, tier: Int, @StringRes name: Int, val cost: Int, modifier: Double,
    @DrawableRes val imageNorth: Int, @DrawableRes val imageEast: Int, @DrawableRes val imageCorner: Int, @DrawableRes val imageDoor: Int
) : Model {
    Fence(1, 1, R.string.wall_fence, 10, 1.0, R.drawable.wall_fence_n, R.drawable.wall_fence_e, R.drawable.wall_fence_c, R.drawable.wall_fence_d),
    BrickFrame(2, 2, R.string.wall_brickframe, 10, 1.0, R.drawable.wall_brick_frame_n, R.drawable.wall_brick_frame_e, R.drawable.wall_brick_frame_c, R.drawable.wall_brick_frame_d),
    BrickPartial(3, 3, R.string.wall_brickpartial, 10, 1.0, R.drawable.wall_brick_partial_n, R.drawable.wall_brick_partial_e, R.drawable.wall_brick_partial_c, R.drawable.wall_brick_partial_d),
    Brick(4, 4, R.string.wall_brick, 100, 1.1, R.drawable.wall_brick_n, R.drawable.wall_brick_e, R.drawable.wall_brick_c, R.drawable.wall_brick_d),
    BrickNoSupport(5, 5, R.string.wall_bricknosupport, 100, 1.1, R.drawable.wall_brick_no_support_n, R.drawable.wall_brick_no_support_e, R.drawable.wall_brick_no_support_c, R.drawable.wall_brick_no_support_d),
    BrickWindow(6, 6, R.string.wall_brickwindow, 100, 1.1, R.drawable.wall_brick_window_n, R.drawable.wall_brick_window_e, R.drawable.wall_brick_window_c, R.drawable.wall_brick_window_d),
    StoneSmall(7, 7, R.string.wall_stonesmall, 1000, 1.2, R.drawable.wall_stone_small_n, R.drawable.wall_stone_small_e, R.drawable.wall_stone_small_c, R.drawable.wall_stone_small_d),
    Stone(8, 8, R.string.wall_stone, 1000, 1.2, R.drawable.wall_stone_n, R.drawable.wall_stone_e, R.drawable.wall_stone_c, R.drawable.wall_stone_d),
    StoneHoles(9, 9, R.string.wall_stoneholes, 1000, 1.2, R.drawable.wall_stone_holes_n, R.drawable.wall_stone_holes_e, R.drawable.wall_stone_c, R.drawable.wall_stone_holes_d),
    StoneWindow(10, 10, R.string.wall_stonewindow, 1000, 1.2, R.drawable.wall_stone_window_n, R.drawable.wall_stone_window_e, R.drawable.wall_stone_c, R.drawable.wall_stone_window_d),
}