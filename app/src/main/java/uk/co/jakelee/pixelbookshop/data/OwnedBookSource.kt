package uk.co.jakelee.pixelbookshop.data

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R

enum class OwnedBookSource(val id: Int, @StringRes name: Int) {
    Gift(1, R.string.source_gift),
    Store(2, R.string.source_store),
    Warehouse(3, R.string.source_warehouse)
}