package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.PlayerDao

class PlayerRepository(private val playerDao: PlayerDao) {

    val name = playerDao.getName()
    val xp = playerDao.getXp()
    val coins = playerDao.getCoins()
    val timeStarted: LiveData<Long> = playerDao.getTimeStarted()
}