package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Wall(
    override val id: Int, val tier: Int, @StringRes val title: Int, val cost: Int, modifier: Double,
    @DrawableRes val imageNorth: Int, @DrawableRes val imageEast: Int, @DrawableRes val imageCorner: Int,
    @DrawableRes val imageDoorNorth: Int, @DrawableRes val imageDoorEast: Int
) : Model {
    Fence(1, 100, R.string.wall_fence, 10, 1.0,
        R.drawable.wall_fence_n, R.drawable.wall_fence_e, R.drawable.wall_fence_c, R.drawable.wall_fence_door_n, R.drawable.wall_fence_door_e),
    BrickFrame(2, 200, R.string.wall_brickframe, 10, 1.0,
        R.drawable.wall_brick_frame_n, R.drawable.wall_brick_frame_e, R.drawable.wall_brick_frame_c, R.drawable.wall_brick_frame_door_n, R.drawable.wall_brick_frame_door_e),
    BrickPartial(3, 300, R.string.wall_brickpartial, 10, 1.0,
        R.drawable.wall_brick_partial_n, R.drawable.wall_brick_partial_e, R.drawable.wall_brick_partial_c, R.drawable.wall_brick_partial_door_n, R.drawable.wall_brick_partial_door_e),
    Brick(4, 400, R.string.wall_brick, 100, 1.1,
        R.drawable.wall_brick_n, R.drawable.wall_brick_e, R.drawable.wall_brick_c, R.drawable.wall_brick_door_n, R.drawable.wall_brick_door_e),
    BrickNoSupport(5, 500, R.string.wall_bricknosupport, 100, 1.1,
        R.drawable.wall_brick_no_support_n, R.drawable.wall_brick_no_support_e, R.drawable.wall_brick_no_support_c, R.drawable.wall_brick_no_support_door_n, R.drawable.wall_brick_no_support_door_e),
    BrickWindow(6, 600, R.string.wall_brickwindow, 100, 1.1,
        R.drawable.wall_brick_window_n, R.drawable.wall_brick_window_e, R.drawable.wall_brick_window_c, R.drawable.wall_brick_window_door_n, R.drawable.wall_brick_window_door_e),
    StoneSmall(7, 700, R.string.wall_stonesmall, 1000, 1.2,
        R.drawable.wall_stone_small_n, R.drawable.wall_stone_small_e, R.drawable.wall_stone_small_c, R.drawable.wall_stone_small_door_n, R.drawable.wall_stone_small_door_e),
    Stone(8, 800, R.string.wall_stone, 1000, 1.2,
        R.drawable.wall_stone_n, R.drawable.wall_stone_e, R.drawable.wall_stone_c, R.drawable.wall_stone_door_n, R.drawable.wall_stone_door_e),
    StoneHoles(9, 900, R.string.wall_stoneholes, 1000, 1.2,
        R.drawable.wall_stone_holes_n, R.drawable.wall_stone_holes_e, R.drawable.wall_stone_c, R.drawable.wall_stone_holes_door_n, R.drawable.wall_stone_holes_door_e),
    StoneWindow(10, 1000, R.string.wall_stonewindow, 1000, 1.2,
        R.drawable.wall_stone_window_n, R.drawable.wall_stone_window_e, R.drawable.wall_stone_c, R.drawable.wall_stone_window_door_n, R.drawable.wall_stone_window_door_e),
}