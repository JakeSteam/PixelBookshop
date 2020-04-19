package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedFloorDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.lookups.Floor

class OwnedFloorRepository(private val ownedFloorDao: OwnedFloorDao, shopId: Int) {

    val allFloor: LiveData<List<OwnedFloor>> = ownedFloorDao.get(shopId)

    suspend fun insert(ownedFloor: List<OwnedFloor>) {
        ownedFloorDao.insert(ownedFloor)
    }

    suspend fun upgradeFloor(ownedFloor: OwnedFloor, nextFloor: Floor) {
        ownedFloor.floor = nextFloor
        ownedFloorDao.update(ownedFloor)
    }

    suspend fun rotateFloor(ownedFloor: OwnedFloor) {
        ownedFloor.isFacingEast = !ownedFloor.isFacingEast
        ownedFloorDao.update(ownedFloor)
    }
}