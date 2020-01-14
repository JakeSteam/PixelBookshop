package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val author: String,
    @ColumnInfo val owned: Int
)