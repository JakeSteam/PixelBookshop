package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.lookups.*

@Entity
data class OwnedBook(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo val book: Book,
    @ColumnInfo val ownedFurnitureId: Int?,
    @ColumnInfo val bookDefect: OwnedBookDefect,
    @ColumnInfo val bookQuality: OwnedBookQuality,
    @ColumnInfo val bookSource: OwnedBookSource,
    @ColumnInfo val bookType: OwnedBookType
): Model