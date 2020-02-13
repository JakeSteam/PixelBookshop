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

    suspend fun upgradeWall(wall: Wall, id: Int) {
        val newWall = when (wall) {
            Wall.StoneWindow -> Wall.Fence
            Wall.Fence -> Wall.BrickFrame
            Wall.BrickFrame -> Wall.BrickPartial
            Wall.BrickPartial -> Wall.Brick
            Wall.Brick -> Wall.BrickNoSupport
            Wall.BrickNoSupport -> Wall.BrickWindow
            Wall.BrickWindow -> Wall.StoneSmall
            Wall.StoneSmall -> Wall.Stone
            Wall.Stone -> Wall.StoneHoles
            Wall.StoneHoles -> Wall.StoneWindow
        }
        shopDao.setWall(newWall, id)
    }
}