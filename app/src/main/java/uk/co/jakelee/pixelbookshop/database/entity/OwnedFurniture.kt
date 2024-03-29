package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.*
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Furniture

@Entity(indices = [Index("x", "y"), Index("shopId")],
    foreignKeys = [ForeignKey(
        entity = Shop::class,
        parentColumns = ["id"],
        childColumns = ["shopId"],
        onDelete = ForeignKey.CASCADE)])
data class OwnedFurniture(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo override val shopId: Int,
    @ColumnInfo override var x: Int,
    @ColumnInfo override var y: Int,
    @ColumnInfo override var isFacingEast: Boolean,
    @ColumnInfo var furniture: Furniture
) : Tile, Model

data class OwnedFurnitureWithOwnedBooks(
    @Embedded val ownedFurniture: OwnedFurniture,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownedFurnitureId"
    )
    val ownedBooks: List<OwnedBook>
)