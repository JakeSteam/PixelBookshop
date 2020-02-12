package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.utils.Xp

class PlayerRepository(private val playerDao: PlayerDao) {

    val name = playerDao.getName()
    val date = playerDao.getDateTime()
    val xp = playerDao.getXp()
    val coins = playerDao.getCoins()
    val player = playerDao.getPlayer()

    suspend fun canPurchase(cost: Int, level: Int): Boolean {
        return Xp.xpToLevel(player.value?.xp ?: 0) >= level
                && cost <= player.value?.coins ?: 0
    }

    suspend fun purchase(cost: Int) {
        playerDao.addXp(cost)
        playerDao.removeCoins(cost)
    }

    suspend fun addXp(newXp: Int) {
        playerDao.addXp(newXp)
    }

    suspend fun removeCoins(coins: Int) {
        playerDao.removeCoins(coins)
    }
}