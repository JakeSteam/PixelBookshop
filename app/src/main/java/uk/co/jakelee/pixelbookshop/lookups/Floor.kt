package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Floor (override val id: Int, val tier: Int, @StringRes val title: Int, val cost: Int, modifier: Double,
                  @DrawableRes val imageNorth: Int, @DrawableRes val imageEast: Int): Model {
    Dirt(1, 100, R.string.floor_dirt, 10, 1.0, R.drawable.floor_dirt_n, R.drawable.floor_dirt_e),
    WoodOld(2, 200, R.string.floor_wood_old, 100, 1.1, R.drawable.floor_planks_old_n, R.drawable.floor_planks_old_e),
    Wood(3, 300, R.string.floor_wood, 100, 1.1, R.drawable.floor_planks_n, R.drawable.floor_planks_e),
    StoneUneven(4, 400, R.string.floor_stone_uneven, 1000, 1.2, R.drawable.floor_stones_uneven_n, R.drawable.floor_stones_uneven_e),
    StoneMissing(5, 500, R.string.floor_stone_missing, 1000, 1.2, R.drawable.floor_stones_missing_n, R.drawable.floor_stones_missing_e),
    Stone(6, 600, R.string.floor_stone, 1000, 1.2, R.drawable.floor_stones_n, R.drawable.floor_stones_e)
}