package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.lookups.Furniture
import uk.co.jakelee.pixelbookshop.utils.Xp
import java.math.BigDecimal

class PlayerRepository(private val playerDao: PlayerDao) {

    val name = playerDao.getName()
    val date = playerDao.getDateTime()
    val xp = playerDao.getXp()
    val coins = playerDao.getCoins()

    suspend fun canPurchase(furniture: Furniture) = canPurchase(furniture.cost, furniture.level)

    suspend fun canPurchase(cost: Int, level: Int) = canPurchase(BigDecimal(cost), level)

    suspend fun canPurchase(cost: BigDecimal, level: Int): Boolean {
        return Xp.xpToLevel(xp.value ?: 0) >= level
                && cost <= coins.value ?: BigDecimal(0)
    }

    suspend fun purchase(cost: Int) = purchase(BigDecimal(cost))

    suspend fun purchase(cost: BigDecimal) = playerDao.removeCoins(cost)

    suspend fun addXp(newXp: Int) = playerDao.addXp(newXp)

    suspend fun addCoins(coins: BigDecimal) = playerDao.addCoins(coins)

    suspend fun addHour() {
        playerDao.addHour()
    }

    suspend fun nextDay() {
        playerDao.nextDay()
    }
}