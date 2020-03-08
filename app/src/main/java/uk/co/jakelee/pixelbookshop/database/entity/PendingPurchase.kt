package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.lookups.Visitor

@Entity
data class PendingPurchase(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo val day: Int,
    @ColumnInfo val time: Int,
    @ColumnInfo val visitor: Visitor,
    @ColumnInfo val ownedBookId: Int,
    @ColumnInfo val seatingAreaId: Int
): Model {
    fun toPastPurchase(book: OwnedBook, satisfaction: Int) = PastPurchase(
        0, day, time, visitor, satisfaction, book.book, book.bookDefect, book.bookSource,
        book.bookType
    )
}