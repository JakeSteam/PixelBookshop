package uk.co.jakelee.pixelbookshop.utils

import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.PendingVisit
import uk.co.jakelee.pixelbookshop.lookups.FurnitureType
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class VisitGenerator {

    fun generate(day: Int, furniture: List<OwnedFurnitureWithOwnedBooks>): List<PendingVisit> {
        val randomHelper = RandomHelper()
        val displayFurniture = furniture.filter {
            it.ownedFurniture.furniture.type == FurnitureType.Display && it.ownedBooks.isNotEmpty()
        }
        val decorativeFurniture = furniture.filter {
            it.ownedFurniture.furniture.type == FurnitureType.Decoration
        }
        val seatingFurniture = furniture.filter {
            it.ownedFurniture.furniture.type == FurnitureType.Seating
        }
        val maxBooks = decorativeFurniture.sumBy { it.ownedFurniture.furniture.capacity }

        // Get maximum visitor number (based on total seating capacity)
        // Pick visitor number randomly
        val maxVisitors = seatingFurniture.sumBy { it.ownedFurniture.furniture.capacity }
        val numVisitors = randomHelper.getInt(maxVisitors)

        val a = mutableListOf<PendingVisit>()
        (0 until numVisitors).forEach {
            val visitor = getVisitor()
            val seatingArea = seatingFurniture.random().ownedFurniture
            val numBooks = randomHelper.getInt(maxBooks)
            (0 until numBooks).forEach {
                val display = displayFurniture.random()
                val book = display.ownedBooks.random()
                a.add(PendingVisit(0, day, 9, visitor, book.id, seatingArea.id))
            }
        }
        return a
        // Make sure books don't duplicate
        // Handle no display / decorating / seating furniture

    }

    //data class PendingVisit(val visitor: Visitor, val purchases: List<OwnedBook>, val seatingArea: OwnedFurniture)

    private fun getVisitor(): Visitor {
        val totalWeighting = Visitor.values().sumByDouble { it.frequency }
        val randomNumber = RandomHelper().getDouble(totalWeighting)
        var probabilityIterator = 0.0
        return Visitor.values().firstOrNull {
            probabilityIterator += it.frequency
            probabilityIterator >= randomNumber
        } ?: Visitor.values().first()
    }


}