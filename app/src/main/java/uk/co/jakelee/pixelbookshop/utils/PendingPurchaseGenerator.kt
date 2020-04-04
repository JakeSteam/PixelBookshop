package uk.co.jakelee.pixelbookshop.utils

import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase
import uk.co.jakelee.pixelbookshop.lookups.FurnitureType
import uk.co.jakelee.pixelbookshop.lookups.Visitor
import kotlin.math.max

class PendingPurchaseGenerator {

    fun generate(day: Int, furniture: List<OwnedFurnitureWithOwnedBooks>): List<PendingPurchase> {
        val randomHelper = RandomHelper()
        val seatingFurniture = getSeatingFurniture(furniture)

        val maxPurchases = getMaxPurchases(furniture)
        val maxVisitors = seatingFurniture.sumBy { it.ownedFurniture.furniture.capacity }
        val numVisitors = 1// max(randomHelper.getInt(maxVisitors), 1)

        val pendingPurchases = mutableListOf<PendingPurchase>()
        val allBooks = getDisplayFurniture(furniture)
        (0 until numVisitors).forEach { _ ->
            val visitor = getVisitor()
            val seatingArea = seatingFurniture.random().ownedFurniture
            val numPurchases = max(randomHelper.getInt(maxPurchases), 1)
            (0 until numPurchases).forEach { _ ->
                if (allBooks.isNotEmpty()) {
                    val book = allBooks.random()
                    allBooks.remove(book)
                    val purchaseHour = randomHelper.getInt(GameTimeHelper.END_HOUR)
                    pendingPurchases.add(
                        PendingPurchase(0, day, purchaseHour, visitor, book.id, seatingArea.id)
                    )
                } else { // If we run out of books during selection
                    return pendingPurchases
                }
            }
        }
        return pendingPurchases

    }

    private fun getSeatingFurniture(furniture: List<OwnedFurnitureWithOwnedBooks>) = furniture
        .filter { it.ownedFurniture.furniture.type == FurnitureType.Seating }

    private fun getMaxPurchases(furniture: List<OwnedFurnitureWithOwnedBooks>) = furniture
        .filter { it.ownedFurniture.furniture.type == FurnitureType.Decoration }
        .sumBy { it.ownedFurniture.furniture.capacity }

    private fun getDisplayFurniture(furniture: List<OwnedFurnitureWithOwnedBooks>) = furniture
        .filter { it.ownedFurniture.furniture.type == FurnitureType.Display && it.ownedBooks.isNotEmpty() }
        .flatMap { it.ownedBooks }.toMutableList()

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