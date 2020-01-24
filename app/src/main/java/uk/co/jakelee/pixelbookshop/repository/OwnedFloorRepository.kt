package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFloorDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor

class OwnedFloorRepository(private val ownedFloorDao: OwnedFloorDao) {

    val allFloor: LiveData<List<OwnedFloor>> = ownedFloorDao.getAll()

    suspend fun insert(ownedFloor: OwnedFloor) {
        ownedFloorDao.insert(ownedFloor)
    }
}