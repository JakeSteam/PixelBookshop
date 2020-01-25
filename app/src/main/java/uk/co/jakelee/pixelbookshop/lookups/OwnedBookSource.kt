package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class OwnedBookSource(override val id: Int, @StringRes name: Int): Model {
    Gift(1, R.string.source_gift),
    Store(2, R.string.source_store),
    Warehouse(3, R.string.source_warehouse)
}