package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["x", "y"])
data class OwnedFloor(
    @ColumnInfo val x: Int,
    @ColumnInfo val y: Int,
    @ColumnInfo val exists: Boolean
)