package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.ShopDao
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Wall

class ShopRepository(private val shopDao: ShopDao, id: Int) {

    val wall: LiveData<WallInfo> = shopDao.getWall(id)

    suspend fun setDoor(x: Int, y: Int, shopId: Int) {
        val onY = x == 0
        val position = if (onY) y else x
        shopDao.setDoor(!onY, position, shopId)
    }

    suspend fun upgradeWall(nextWall: Wall, shopId: Int) {
        shopDao.setWall(nextWall, shopId)
    }
}