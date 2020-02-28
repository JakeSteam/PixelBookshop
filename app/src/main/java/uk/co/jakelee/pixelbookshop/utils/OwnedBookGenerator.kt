package uk.co.jakelee.pixelbookshop.utils

import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.lookups.*

class OwnedBookGenerator(private val source: OwnedBookSource) {
    private val randomHelper = RandomHelper()

    fun generate(number: Int = 1) = (0..number).map {
        val quality = getBookQuality()
        OwnedBook(
            0, getBook(), null, getBookDefect(quality.canHaveDefects),
            quality, source, getBookType()
        )
    }

    private fun getBook(): Book {
        val totalWeighting = Book.values().sumByDouble { it.rarity.frequency }
        val randomNumber = randomHelper.getDouble(totalWeighting)
        var probabilityIterator = 0.0
        return Book.values().firstOrNull {
            probabilityIterator += it.rarity.frequency
            probabilityIterator >= randomNumber
        } ?: Book.values().first()
    }

    private fun getBookQuality(): OwnedBookQuality {
        val totalWeighting = OwnedBookQuality.values().sumByDouble { it.frequency }
        val randomNumber = randomHelper.getDouble(totalWeighting)
        var probabilityIterator = 0.0
        return OwnedBookQuality.values().firstOrNull {
            probabilityIterator += it.frequency
            probabilityIterator >= randomNumber
        } ?: OwnedBookQuality.values().first()
    }

    private fun getBookType(): OwnedBookType {
        val totalWeighting = OwnedBookType.values().sumByDouble { it.frequency }
        val randomNumber = randomHelper.getDouble(totalWeighting)
        var probabilityIterator = 0.0
        return OwnedBookType.values().firstOrNull {
            probabilityIterator += it.frequency
            probabilityIterator >= randomNumber
        } ?: OwnedBookType.values().first()
    }

    private fun getBookDefect(canHaveDefects: Boolean): OwnedBookDefect {
        if (canHaveDefects) { OwnedBookDefect.None }

        val totalWeighting = OwnedBookDefect.values().sumByDouble { it.frequency }
        val randomNumber = randomHelper.getDouble(totalWeighting)
        var probabilityIterator = 0.0
        return OwnedBookDefect.values().firstOrNull {
            probabilityIterator += it.frequency
            probabilityIterator >= randomNumber
        } ?: OwnedBookDefect.values().first()
    }
}