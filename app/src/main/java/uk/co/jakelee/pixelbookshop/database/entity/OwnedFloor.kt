package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Floor

@Entity(primaryKeys = ["x", "y"],
    foreignKeys = [ForeignKey(
        entity = Shop::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE)],
    indices = [Index("shopId")])
data class OwnedFloor(
    @ColumnInfo override val shopId: Int,
    @ColumnInfo override var x: Int,
    @ColumnInfo override var y: Int,
    @ColumnInfo override var isFacingEast: Boolean,
    @ColumnInfo var floor: Floor?
) : Tile