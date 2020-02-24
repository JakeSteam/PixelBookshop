package uk.co.jakelee.pixelbookshop.scripts

import java.net.URL

fun main() {
    val apiKey = "" // https://www.goodreads.com/api/keys
    """
https://www.goodreads.com/book/show/48855.The_Diary_of_a_Young_Girl
https://www.goodreads.com/book/show/138685.Dissolution
https://www.goodreads.com/book/show/36402034-do-androids-dream-of-electric-sheep
https://www.goodreads.com/book/show/17245.Dracula
https://www.goodreads.com/book/show/17491.The_Enchanted_Wood
https://www.goodreads.com/book/show/11713.The_English_Patient
https://www.goodreads.com/book/show/7745.Fear_and_Loathing_in_Las_Vegas
https://www.goodreads.com/book/show/34.The_Fellowship_of_the_Ring
https://www.goodreads.com/book/show/36576608-flowers-for-algernon
https://www.goodreads.com/book/show/35031085-frankenstein
""".split("\n")
        .filter { it.isNotEmpty() }
        .forEach { url ->
            val id = URL(url).path
                .replace(".", "-")
                .substringAfterLast("/")
                .substringBefore("-")
            val xml = URL("https://www.goodreads.com/book/show/$id.xml?key=$apiKey").readText()
            val genre = getGenre(xml.substringAfter("<popular_shelves>").substringBefore("</popular_shelves>"))
            val rarity = getRarity(xml.substringAfter("<ratings_count type=\"integer\">").substringBefore("</ratings_count>"))
            val title = getTitle(xml.substringAfter("<title>").substringBefore("</title>"))
            val name = xml.substringAfter("<name>").substringBefore("</name>")
            val authorSurname = name.substringAfterLast(" ")
            val authorFirstName = name.substringBeforeLast(" ")
            val published = xml.substringAfter("<original_publication_year type=\"integer\">").substringBefore("</original_publication_year>")

            val sanitisedTitle = title.filter { it.isLetterOrDigit() }
            val sanitisedSurname = authorSurname.filter { it.isLetterOrDigit() }

            println("$sanitisedSurname$sanitisedTitle($id, BookGenre.$genre, BookRarity.$rarity, \"$title\", \"$authorFirstName\", \"$authorSurname\", $published, \"$url\"),")
        }
}

fun getTitle(title: String): String {
    if (title.startsWith("<![")) {
        return title.substringAfter("<![CDATA[")
            .substringBefore("(")
            .substringBefore("]]>")
            .trim()
    }
    return title
}

fun getGenre(genres: String): String {
    genres
        .split("\n")
        .map { it.substringAfter("<shelf name=\"").substringBefore("\" count=")}
        .forEach {  genre ->
            when(genre) {
                "action-adventure" -> return "ActionAdventure"
                "anthology" -> return "Anthology"
                "comic" -> return "Comic"
                "crime" -> return "Crime"
                "drama" -> return "Drama"
                "fairytale" -> return "Fairytale"
                "fanfiction", "fan-fiction" -> return "Fanfiction"
                "fantasy" -> return "Fantasy"
                "historical-fiction" -> return "HistoricalFiction"
                "horror" -> return "Horror"
                "humor", "humour" -> return "Humor"
                "legend" -> return "Legend"
                "mystery" -> return "Mystery"
                "mythology" -> return "Mythology"
                "poetry" -> return "Poetry"
                "romance" -> return "Romance"
                "satire" -> return "Satire"
                "science-fiction", "sci-fi", "scifi" -> return "ScienceFiction"
                "thriller" -> return "Thriller"
                "autobiography" -> return "Autobiography"
                "biography" -> return "Biography"
                "essay", "essays" -> return "Essay"
                "narrative" -> return "Narrative"
                "periodical" -> return "Periodical"
                "reference", "non-fiction", "nonfiction" -> return "Reference"
                "selfhelp", "self-help" -> return "SelfHelp"
                "speech", "speeches" -> return "Speech"
                "textbook", "text-book" -> return "Textbook"
            }
        }
    return ""
}

fun getRarity(ratingsString: String): String {
    val ratings = ratingsString.toInt()
    return when {
        ratings < 10 -> "Unique"
        ratings < 100 -> "ExtremelyRare"
        ratings < 1000 -> "VeryRare"
        ratings < 10000 -> "Rare"
        ratings < 100000 -> "Uncommon"
        else -> "Common"
    }
}