package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Book(
    override val id: Int,
    val genre: BookGenre,
    val rarity: BookRarity,
    @StringRes val title: Int,
    @StringRes val author: Int,
    val published: Int,
    val url: String
): Model {
    TolkienHobbit(1, BookGenre.ActionAdventure, BookRarity.Common,
        R.string.title_hobbit, R.string.author_tolkien, 1937, "https://www.goodreads.com/book/show/5907.The_Hobbit_or_There_and_Back_Again"),
    Orwell1984(2, BookGenre.ScienceFiction, BookRarity.Uncommon,
        R.string.title_1984, R.string.author_orwell, 1949, "https://www.goodreads.com/book/show/40961427-1984"),
    MooreVendetta(3, BookGenre.Comic, BookRarity.Uncommon,
        R.string.title_vforvendetta, R.string.author_moore, 2005, "https://www.goodreads.com/book/show/5805.V_for_Vendetta"),
    MartinGameOfThrones(4, BookGenre.Fantasy, BookRarity.Common,
        R.string.title_gameofthrones, R.string.author_rrmartin, 1996, "https://www.goodreads.com/book/show/13496.A_Game_of_Thrones")
}