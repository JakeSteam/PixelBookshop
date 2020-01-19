package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OwnedFurniture(
    @PrimaryKey val id: Int,
    @ColumnInfo val x: Int,
    @ColumnInfo val y: Int,
    @ColumnInfo val facingEast: Boolean,
    @ColumnInfo val furnitureId: Int
)