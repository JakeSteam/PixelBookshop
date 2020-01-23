package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class OwnedBookDefect(override val id: Int, @StringRes name: Int, frequency: Double, modifier: Double): Model {
    None(1, R.string.defect_none, 1.0, 1.0),
    FoldedPages(2, R.string.defect_foldedpages, 0.05, 0.9),
    StainedPages(3, R.string.defect_stainedpages, 0.05, 0.8),
    Annotations(4, R.string.defect_annotations, 0.05, 0.7),
    MissingCover(5, R.string.defect_missingcover, 0.03, 0.4),
    Stickered(6, R.string.defect_stickered, 0.07, 0.85),
    MissingPages(7, R.string.defect_missingpages, 0.03, 0.5)
}