package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFloorDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.lookups.Floor

class OwnedFloorRepository(private val ownedFloorDao: OwnedFloorDao) {

    val allFloor: LiveData<List<OwnedFloor>> = ownedFloorDao.getAll()

    suspend fun insert(ownedFloor: OwnedFloor) {
        ownedFloorDao.insert(ownedFloor)
    }

    suspend fun upgradeFloor(ownedFloor: OwnedFloor, nextFloor: Floor) {
        ownedFloor.floor = nextFloor
        insert(ownedFloor)
    }

    suspend fun rotateFloor(ownedFloor: OwnedFloor) {
        ownedFloor.isFacingEast = !ownedFloor.isFacingEast
        insert(ownedFloor)
    }
}