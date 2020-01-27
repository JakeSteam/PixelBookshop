package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.ShopDao
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Wall

class ShopRepository(private val shopDao: ShopDao, id: Int) {

    val wall: LiveData<WallInfo> = shopDao.getWall(id)

    suspend fun changeWall(wall: Wall, id: Int) {
        shopDao.setWall(wall, id)
    }
}