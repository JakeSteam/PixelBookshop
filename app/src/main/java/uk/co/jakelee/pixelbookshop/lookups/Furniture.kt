package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Furniture(
    type: FurnitureType, override val id: Int, tier: Int, @StringRes name: Int, cost: Int,
    capacity: Int, // Decoration: Range. Display: Books shown. Seating: Seats. Storage: Books stored.
    level: Int, // Level required to purchase
    rarity: BookRarity, // Decoration: N/A. Display: Max rarity shown. Seating: Max rarity read there. Storage: Books extra capacity applies too.
    @DrawableRes val imageNorth: Int, @DrawableRes val imageEast: Int,
    @DrawableRes val imageNorthFilled: Int? = null, @DrawableRes val imageEastFilled: Int? = null
) : Model {
    // Decorations
    Rug(
        FurnitureType.Decoration,
        1001,
        1,
        R.string.decoration_rug,
        100,
        0,
        1,
        BookRarity.Common,
        R.drawable.furniture_rug_n, R.drawable.furniture_rug_e
    ),
    SingleCandle(
        FurnitureType.Decoration,
        1002,
        2,
        R.string.decoration_singlecandle,
        100,
        1,
        1,
        BookRarity.Common,
        R.drawable.furniture_single_candle_n, R.drawable.furniture_single_candle_e
    ),
    TripleCandle(
        FurnitureType.Decoration,
        1003,
        3,
        R.string.decoration_triplecandle,
        200,
        2,
        10,
        BookRarity.Common,
        R.drawable.furniture_triple_candle_n, R.drawable.furniture_triple_candle_e
    ),

    // Displays
    Lectern(
        FurnitureType.Display, 2001, 1, R.string.display_lectern, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_lectern_n, R.drawable.furniture_lectern_e, R.drawable.furniture_lectern_nf, R.drawable.furniture_lectern_ef),
    Shelf(
        FurnitureType.Display, 2002, 2, R.string.display_shelf, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_shelf_n, R.drawable.furniture_shelf_e, R.drawable.furniture_shelf_nf, R.drawable.furniture_shelf_ef),
    WideShelf(
        FurnitureType.Display, 2003, 3, R.string.display_wideshelf, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_wide_shelf_n, R.drawable.furniture_wide_shelf_e, R.drawable.furniture_wide_shelf_nf, R.drawable.furniture_wide_shelf_ef),
    DisplayShelf(
        FurnitureType.Display, 2004, 4, R.string.display_displayshelf, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_display_shelf_n, R.drawable.furniture_display_shelf_e, R.drawable.furniture_display_shelf_nf, R.drawable.furniture_display_shelf_ef),
    Bookcase(
        FurnitureType.Display, 2005, 5, R.string.display_bookcase, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_bookcase_n, R.drawable.furniture_bookcase_e, R.drawable.furniture_bookcase_nf, R.drawable.furniture_bookcase_ef),
    WideBookcase(
        FurnitureType.Display, 2006, 6, R.string.display_widebookcase, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_wide_bookcase_n, R.drawable.furniture_wide_bookcase_e, R.drawable.furniture_wide_bookcase_nf, R.drawable.furniture_wide_bookcase_ef),
    WideBookcaseLadder(
        FurnitureType.Display, 2007, 7, R.string.display_widebookcaseladder, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_wide_bookcase_ladder_n, R.drawable.furniture_wide_bookcase_ladder_e, R.drawable.furniture_wide_bookcase_ladder_nf, R.drawable.furniture_wide_bookcase_ladder_ef),


    // Seating
    Chair(
        FurnitureType.Seating, 3001, 1, R.string.seating_chair, 100, 1, 1, BookRarity.Common,
        R.drawable.furniture_chair_n, R.drawable.furniture_chair_e),
    SmallTable(
        FurnitureType.Seating, 3002, 2, R.string.seating_smalltable, 100, 2, 1, BookRarity.Common,
        R.drawable.furniture_small_table_n, R.drawable.furniture_small_table_e),
    RoundTable(
        FurnitureType.Seating, 3003, 3, R.string.seating_roundtable, 100, 4, 1, BookRarity.Common,
        R.drawable.furniture_round_table_n, R.drawable.furniture_round_table_e),
    PlainTable(
        FurnitureType.Seating, 3004, 4, R.string.seating_plaintable, 100, 4, 1, BookRarity.Common,
        R.drawable.furniture_plain_table_n, R.drawable.furniture_plain_table_e),
    DecoratedTable(
        FurnitureType.Seating, 3005, 5, R.string.seating_decoratedtable, 100, 4, 1, BookRarity.Common,
        R.drawable.furniture_decorated_table_n, R.drawable.furniture_decorated_table_e),
    FancyTable(
        FurnitureType.Seating, 3006, 6, R.string.seating_fancytable, 100, 4, 1, BookRarity.Common,
        R.drawable.furniture_fancy_table_n, R.drawable.furniture_fancy_table_e),

    // Storage
    Chest(
        FurnitureType.Storage, 4001, 1, R.string.storage_chest, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_chest_n, R.drawable.furniture_chest_e),
    Crate(
        FurnitureType.Storage, 4002, 2, R.string.storage_crate, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_crate_n, R.drawable.furniture_crate_e),
    TripleCrate(
        FurnitureType.Storage, 4003, 3, R.string.storage_triplecrate, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_triple_crate_n, R.drawable.furniture_triple_crate_e
    ),
    Barrel(
        FurnitureType.Storage, 4004, 4, R.string.storage_barrel, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_barrel_n, R.drawable.furniture_barrel_e),
    TripleBarrel(
        FurnitureType.Storage, 4005, 4, R.string.storage_triplebarrel, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_triple_barrel_n, R.drawable.furniture_triple_barrel_e),
    QuintupleBarrel(
        FurnitureType.Storage, 4006, 5, R.string.storage_quintuplebarrel, 100, 10, 1, BookRarity.Common,
        R.drawable.furniture_quintuple_barrel_n, R.drawable.furniture_quintuple_barrel_e)
}