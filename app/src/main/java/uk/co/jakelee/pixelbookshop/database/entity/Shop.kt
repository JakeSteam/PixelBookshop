package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Shop(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val wallId: Int,
    @ColumnInfo val timeStarted: Long
)