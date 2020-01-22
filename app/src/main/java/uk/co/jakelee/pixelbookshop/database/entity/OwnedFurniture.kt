package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import uk.co.jakelee.pixelbookshop.interfaces.Tile

@Entity(primaryKeys = ["x", "y"])
data class OwnedFurniture(
    @ColumnInfo override val x: Int,
    @ColumnInfo override val y: Int,
    @ColumnInfo val facingEast: Boolean,
    @ColumnInfo val furnitureId: Int
) : Tile