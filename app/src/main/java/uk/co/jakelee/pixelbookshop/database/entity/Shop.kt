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
    val isDoorOnX: Boolean,
    val doorPosition: Int
) {
    private fun isDoor(x: Int, y: Int) = isDoorOnX && doorPosition == x
            || !isDoorOnX && doorPosition == y

    fun getAsset(x: Int, y: Int, maxY: Int) = when {
        isDoor(x, y) -> wall.imageDoor
        x == 0 && y == maxY -> wall.imageCorner
        x == 0 -> wall.imageEast
        y == maxY -> wall.imageNorth
        else -> null
    }
}