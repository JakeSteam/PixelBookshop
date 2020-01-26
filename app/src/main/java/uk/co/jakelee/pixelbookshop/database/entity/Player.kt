package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakelee.pixelbookshop.lookups.Wall

@Entity
data class Player(
    @PrimaryKey val name: String,
    @ColumnInfo val xp: Long,
    @ColumnInfo val coins: Long,
    @Embedded var wallInfo: WallInfo,
    @ColumnInfo val started: Long
)

data class WallInfo(
    var wall: Wall,
    val isX: Boolean,
    val position: Int
)