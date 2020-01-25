package uk.co.jakelee.pixelbookshop.lookups

import androidx.annotation.StringRes
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class BookGenre(override val id: Int, @StringRes name: Int, isFiction: Boolean): Model {
    ActionAdventure(1, R.string.genre_actionadventure, true),
    Anthology(2, R.string.genre_anthology, true),
    Comic(4, R.string.genre_comic, true),
    Crime(5, R.string.genre_crime, true),
    Drama(6, R.string.genre_drama, true),
    Fairytale(7, R.string.genre_fairytale, true),
    Fanfiction(8, R.string.genre_fanfiction, true),
    Fantasy(9, R.string.genre_fantasy, true),
    HistoricalFiction(10, R.string.genre_historicalfiction, true),
    Horror(11, R.string.genre_horror, true),
    Humor(12, R.string.genre_humor, true),
    Legend(13, R.string.genre_legend, true),
    Mystery(14, R.string.genre_mystery, true),
    Mythology(15, R.string.genre_mythology, true),
    Poetry(16, R.string.genre_poetry, true),
    Romance(17, R.string.genre_romance, true),
    Satire(18, R.string.genre_satire, true),
    ScienceFiction(19, R.string.genre_sciencefiction, true),
    Thriller(20, R.string.genre_thriller, true),
    Autobiography(21, R.string.genre_autobiography, false),
    Biography(22, R.string.genre_biography, false),
    Essay(23, R.string.genre_essay, false),
    Narrative(24, R.string.genre_narrative, false),
    Periodical(25, R.string.genre_periodical, false),
    Reference(26, R.string.genre_reference, false),
    SelfHelp(27, R.string.genre_selfhelp, false),
    Speech(28, R.string.genre_speech, false),
    Textbook(29, R.string.genre_textbook, false),
}