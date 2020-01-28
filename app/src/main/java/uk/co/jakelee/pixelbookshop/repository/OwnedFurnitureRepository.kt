package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFurnitureDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks

class OwnedFurnitureRepository(private val ownedFurnitureDao: OwnedFurnitureDao) {

    val allFurniture: LiveData<List<OwnedFurniture>> = ownedFurnitureDao.getAll()

    val allFurnitureWithBooks: LiveData<List<OwnedFurnitureWithOwnedBooks>> = ownedFurnitureDao.getAllWithBooks()

    suspend fun insert(ownedFurniture: OwnedFurniture) {
        ownedFurnitureDao.insert(ownedFurniture)
    }
}