package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Floor

@Entity(primaryKeys = ["x", "y"],
    foreignKeys = [ForeignKey(
        entity = Shop::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE)])
data class OwnedFloor(
    @ColumnInfo override val shopId: Int,
    @ColumnInfo override val x: Int,
    @ColumnInfo override val y: Int,
    @ColumnInfo override var isFacingEast: Boolean,
    @ColumnInfo var floor: Floor?
) : Tile