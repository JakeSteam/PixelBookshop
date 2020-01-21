package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OwnedFloor(
    @PrimaryKey val id: Int,
    @ColumnInfo val x: Int,
    @ColumnInfo val y: Int,
    @ColumnInfo val exists: Boolean
)