package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.lookups.*

@Entity
data class PastPurchase(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo val shopId: Int,
    @ColumnInfo val day: Int,
    @ColumnInfo val hour: Int,
    @ColumnInfo val visitor: Visitor,
    @ColumnInfo val satisfaction: Int,
    @ColumnInfo val book: Book,
    @ColumnInfo val bookDefect: OwnedBookDefect,
    @ColumnInfo val bookSource: OwnedBookSource,
    @ColumnInfo val bookType: OwnedBookType
): Model