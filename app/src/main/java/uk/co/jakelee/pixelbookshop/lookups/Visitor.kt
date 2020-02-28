package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Visitor(
    override val id: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val frequency: Double,
    val modifier: Double,
    val bookPreference: Pair<Book, Double>,
    val bookGenrePreference: Pair<BookGenre, Double>,
    val bookRarityPreference: Pair<BookRarity, Double>,
    val bookDefectPreference: Pair<OwnedBookDefect, Double>,
    val bookSourcePreference: Pair<OwnedBookSource, Double>,
    val bookTypePreference: Pair<OwnedBookType, Double>
): Model {
    TestVisitor(0, R.string.visitor_testVisitor_name, R.string.visitor_testVisitor_desc, 1.0, 1.1,
        Pair(Book.HawkingABriefHistoryofTime, 1.1),
        Pair(BookGenre.ScienceFiction, 1.25),
        Pair(BookRarity.Uncommon, 1.12),
        Pair(OwnedBookDefect.None, 1.0),
        Pair(OwnedBookSource.Store, 1.0),
        Pair(OwnedBookType.SignedEdition, 2.0)
    )
}