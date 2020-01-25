package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Book(
    override val id: Int, genre: BookGenre, rarity: BookRarity, @StringRes title: Int, @StringRes author: Int, published: Int, @StringRes url: Int
): Model {
    TolkienHobbit(1, BookGenre.ActionAdventure, BookRarity.Common,
        R.string.title_hobbit, R.string.author_tolkien, 1937, R.string.url_tolkienhobbit),
    Orwell1984(2, BookGenre.ScienceFiction, BookRarity.Uncommon,
        R.string.title_1984, R.string.author_orwell, 1949, R.string.url_orwell1984),
    MooreVendetta(3, BookGenre.Comic, BookRarity.Uncommon,
        R.string.title_vforvendetta, R.string.author_moore, 2005, R.string.url_moorevendetta),
    MartinGameOfThrones(4, BookGenre.Fantasy, BookRarity.Common,
        R.string.title_gameofthrones, R.string.author_rrmartin, 1996, R.string.url_martingameofthrones)
}