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
    fun isDoor(x: Int, y: Int, maxY: Int) = isXTileDoor(x, y, maxY) || isYTileDoor(x, y)

    private fun isXTileDoor(x: Int, y: Int, maxY: Int) = isDoorOnX && x == doorPosition && y == maxY

    private fun isYTileDoor(x: Int, y: Int, minX: Int = 0) = !isDoorOnX && y == doorPosition && x == minX

    fun getAsset(x: Int, y: Int, maxY: Int) = when {
        isXTileDoor(x, y, maxY) -> wall.imageDoorNorth
        isYTileDoor(x, y) -> wall.imageDoorEast
        x == 0 && y == maxY -> wall.imageCorner
        x == 0 -> wall.imageEast
        y == maxY -> wall.imageNorth
        else -> null
    }
}