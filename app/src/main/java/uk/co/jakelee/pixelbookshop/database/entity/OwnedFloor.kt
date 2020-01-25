package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Floor

@Entity(primaryKeys = ["x", "y"])
data class OwnedFloor(
    @ColumnInfo override val x: Int,
    @ColumnInfo override val y: Int,
    @ColumnInfo var floor: Floor?
) : Tile