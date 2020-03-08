package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PastPurchaseDao
import uk.co.jakelee.pixelbookshop.database.entity.PastPurchase
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class PastPurchaseRepository(private val pastPurchaseDao: PastPurchaseDao) {

    suspend fun addPurchase(vararg pastPurchase: PastPurchase) = pastPurchaseDao.insert(*pastPurchase)

    fun getRecentPurchases() = pastPurchaseDao.getLatestPurchases()

    fun getRecentPurchasesByVisitor(visitor: Visitor) = pastPurchaseDao.getLatestPurchasesByVisitor(visitor)

    suspend fun tidyUpPurchases() = pastPurchaseDao.deleteAllOlderPurchases()
}