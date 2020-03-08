package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PastPurchaseDao
import uk.co.jakelee.pixelbookshop.database.entity.PastPurchase
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class PastPurchaseRepository(private val pastPurchaseDao: PastPurchaseDao) {

    suspend fun addPurchases(pastPurchases: List<PastPurchase>) = pastPurchaseDao.insert(pastPurchases)

    fun getRecentPurchases() = pastPurchaseDao.getLatestPurchases()

    fun getRecentPurchasesByVisitor(visitor: Visitor) = pastPurchaseDao.getLatestPurchasesByVisitor(visitor)

    suspend fun tidyUpPurchases() = pastPurchaseDao.deleteAllOlderPurchases()
}