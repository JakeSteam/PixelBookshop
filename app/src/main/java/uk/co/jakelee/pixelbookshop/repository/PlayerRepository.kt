package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Wall

class PlayerRepository(private val playerDao: PlayerDao) {

    val name = playerDao.getName()
    val xp = playerDao.getXp()
    val coins = playerDao.getCoins()
    val wall: LiveData<WallInfo> = playerDao.getWall()
    val timeStarted: LiveData<Long> = playerDao.getTimeStarted()

    suspend fun addXp(newXp: Int) {
        playerDao.addXp(newXp)
    }

    suspend fun changeWall(wall: Wall) {
        playerDao.setWall(wall)
    }
}