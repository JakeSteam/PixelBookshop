package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey val name: String,
    @ColumnInfo val xp: Long,
    @ColumnInfo val coins: Long
)