package uk.co.jakelee.pixelbookshop.model

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Furniture(
    type: FurnitureType, override val id: Int, tier: Int, @StringRes name: Int, cost: Int,
    capacity: Int, // Decoration: Range. Display: Books shown. Seating: Seats. Storage: Books stored.
    level: Int, // Level required to purchase
    rarity: BookRarity // Decoration: N/A. Display: Max rarity shown. Seating: Max rarity read there. Storage: Books extra capacity applies too.
): Model {
    SingleCandle(FurnitureType.Decoration, 1001, 1, R.string.decoration_singlecandle,
        100, 1, 1, BookRarity.Common),
    TripleCandle(FurnitureType.Decoration, 1002, 2, R.string.decoration_triplecandle,
        200, 2, 10, BookRarity.Common),

    Lectern(FurnitureType.Display, 2001, 1, R.string.display_lectern,
        100, 10, 1, BookRarity.Common),
    Shelf(FurnitureType.Display, 2002, 2, R.string.display_shelf,
        200, 20, 5, BookRarity.Uncommon),
    Bookcase(FurnitureType.Display, 2003, 3, R.string.display_bookcase,
        500, 100, 10, BookRarity.Rare),

    Chair(FurnitureType.Seating, 3001, 1, R.string.seating_chair,
        100, 1, 1, BookRarity.Common),
    RoundTable(FurnitureType.Seating, 3002, 2, R.string.seating_roundtable,
        500, 4, 5, BookRarity.Rare),

    SmallCrate(FurnitureType.Storage, 4001, 1, R.string.storage_smallcrate,
        100, 10, 1, BookRarity.Common),
    BigCrate(FurnitureType.Storage, 4002, 2, R.string.storage_bigcrate,
        500, 30, 1, BookRarity.Uncommon)
}