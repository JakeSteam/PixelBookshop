package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OwnedBook(
    @PrimaryKey val id: Int,
    @ColumnInfo val bookId: Int,
    @ColumnInfo val furnitureId: Int,
    @ColumnInfo val bookDefectId: Int,
    @ColumnInfo val bookQualityId: Int,
    @ColumnInfo val bookSourceId: Int,
    @ColumnInfo val bookTypeId: Int
)