package uk.co.jakelee.pixelbookshop.database.entity

import androidx.room.*
import uk.co.jakelee.pixelbookshop.interfaces.Model
import uk.co.jakelee.pixelbookshop.interfaces.Tile
import uk.co.jakelee.pixelbookshop.lookups.Furniture

@Entity(indices = [Index("x", "y")])
data class OwnedFurniture(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo override val x: Int,
    @ColumnInfo override val y: Int,
    @ColumnInfo val facingEast: Boolean,
    @ColumnInfo val furniture: Furniture
) : Tile, Model

data class OwnedFurnitureWithOwnedBooks(
    @Embedded val ownedFurniture: OwnedFurniture,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownedFurnitureId"
    )
    val ownedBooks: List<OwnedBook>
)