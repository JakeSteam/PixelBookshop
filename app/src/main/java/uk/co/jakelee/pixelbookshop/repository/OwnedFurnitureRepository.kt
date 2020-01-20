package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFurnitureDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture

class OwnedFurnitureRepository(private val ownedFurnitureDao: OwnedFurnitureDao) {

    val allFurniture: LiveData<List<OwnedFurniture>> = ownedFurnitureDao.getAll()

    suspend fun insert(ownedFurniture: OwnedFurniture) {
        ownedFurnitureDao.insert(ownedFurniture)
    }
}