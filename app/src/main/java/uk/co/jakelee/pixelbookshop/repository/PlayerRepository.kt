package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao

class PlayerRepository(private val playerDao: PlayerDao) {

    val name = playerDao.getName()
    val xp = playerDao.getXp()
    val coins = playerDao.getCoins()

    suspend fun addXp(newXp: Int) {
        playerDao.addXp(newXp)
    }
}