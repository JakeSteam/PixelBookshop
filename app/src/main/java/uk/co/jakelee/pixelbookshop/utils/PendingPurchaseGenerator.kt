package uk.co.jakelee.pixelbookshop.utils

import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase
import uk.co.jakelee.pixelbookshop.lookups.FurnitureType
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class PendingPurchaseGenerator {

    fun generate(day: Int, furniture: List<OwnedFurnitureWithOwnedBooks>): List<PendingPurchase> {
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

        val maxPurchases = decorativeFurniture.sumBy { it.ownedFurniture.furniture.capacity }
        val maxVisitors = seatingFurniture.sumBy { it.ownedFurniture.furniture.capacity }
        val numVisitors = randomHelper.getInt(maxVisitors)

        val pendingPurchases = mutableListOf<PendingPurchase>()
        val allBooks = displayFurniture.flatMap { it.ownedBooks }.toMutableList()
        (numVisitors/2 until numVisitors).forEach {
            val visitor = getVisitor()
            val seatingArea = seatingFurniture.random().ownedFurniture
            val numPurchases = randomHelper.getInt(maxPurchases)
            (numPurchases/2 until numPurchases).forEach {
                if (allBooks.isNotEmpty()) {
                    val book = allBooks.random()
                    allBooks.remove(book)
                    pendingPurchases.add(
                        PendingPurchase(0, day, 9, visitor, book.id, seatingArea.id)
                    )
                } else { // If we run out of books during selection
                    return pendingPurchases
                }
            }
        }
        return pendingPurchases

    }

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