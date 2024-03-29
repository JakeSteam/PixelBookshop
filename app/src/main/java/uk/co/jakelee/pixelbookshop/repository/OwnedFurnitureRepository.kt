package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFurnitureDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.lookups.Furniture

class OwnedFurnitureRepository(private val ownedFurnitureDao: OwnedFurnitureDao, shopId: Int) {

    val allFurniture: LiveData<List<OwnedFurniture>> = if (shopId > 0) {
        ownedFurnitureDao.get(shopId)
    } else {
        ownedFurnitureDao.getAll()
    }

    val allFurnitureWithBooks: LiveData<List<OwnedFurnitureWithOwnedBooks>> = if (shopId > 0) {
        ownedFurnitureDao.getWithBooks(shopId)
    } else {
        ownedFurnitureDao.getAllWithBooks()
    }

    suspend fun insert(ownedFurniture: OwnedFurniture) {
        ownedFurnitureDao.insert(ownedFurniture)
    }

    suspend fun increaseX(shopId: Int) = ownedFurnitureDao.increaseX(shopId)

    suspend fun increaseY(shopId: Int) = ownedFurnitureDao.increaseY(shopId)

    suspend fun upgradeFurni(ownedFurni: OwnedFurniture, nextFurniture: Furniture) {
        ownedFurni.furniture = nextFurniture
        insert(ownedFurni)
    }

    suspend fun rotateFurni(furniture: OwnedFurniture) {
        furniture.apply { isFacingEast = !isFacingEast }
        insert(furniture)
    }

    suspend fun moveFurni(floor: OwnedFloor, selectedFurni: OwnedFurniture?) {
        val furniOnFloor = ownedFurnitureDao.getByPosition(floor.x, floor.y)
        if (furniOnFloor != null && selectedFurni != null) { // Furniture on tapped tile
           swap(selectedFurni, furniOnFloor)
        } else if (selectedFurni != null) { // We have furniture to move
            selectedFurni.x = floor.x
            selectedFurni.y = floor.y
            insert(selectedFurni)
        }
    }

    suspend fun swap(one: OwnedFurniture, two: OwnedFurniture) {
        val originalX = one.x
        val originalY = one.y
        one.x = two.x
        one.y = two.y
        two.x = originalX
        two.y = originalY
        ownedFurnitureDao.insert(one, two)
    }

    suspend fun assignBooks(furnitureId: Int, bookIds: List<Int>) {
        ownedFurnitureDao.assignBooks(furnitureId, bookIds)
    }
}