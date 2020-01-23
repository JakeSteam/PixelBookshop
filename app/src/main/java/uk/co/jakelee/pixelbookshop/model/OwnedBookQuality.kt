package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class OwnedBookQuality(override val id: Int, @StringRes name: Int, canHaveDefects: Boolean, frequency: Double, modifier: Double): Model {
    New(1, R.string.quality_new, false, 0.1, 1.0),
    Fine(2, R.string.quality_fine, false, 0.2, 0.95),
    VeryGood(3, R.string.quality_verygood, false, 0.4,0.9),
    Good(4, R.string.quality_good, false, 0.6, 0.75),
    Fair(5, R.string.quality_fair, true, 1.0, 0.65),
    Poor(6, R.string.quality_poor, true, 0.6, 0.5)
}