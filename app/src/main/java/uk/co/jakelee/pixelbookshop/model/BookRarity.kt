package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class BookRarity(override val id: Int, @StringRes name: Int, frequency: Double, modifier: Double): Model {
    Unique(1, R.string.rarity_unique, 0.01, 20.0),
    ExtremelyRare(2, R.string.rarity_extremelyrare, 0.1, 3.0),
    VeryRare(3, R.string.rarity_veryrare, 0.2, 2.0),
    Rare(4, R.string.rarity_rare, 0.4, 1.5),
    Uncommon(5, R.string.rarity_uncommon, 0.4, 1.25),
    Common(6, R.string.rarity_common, 1.0, 1.0)
}