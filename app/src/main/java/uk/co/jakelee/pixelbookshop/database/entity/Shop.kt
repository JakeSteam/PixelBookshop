package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.lookups.Wall

@Entity
data class Shop(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @Embedded var wallInfo: WallInfo,
    @ColumnInfo val timeStarted: Long
)

data class WallInfo(
    var wall: Wall,
    val isX: Boolean,
    val position: Int
)