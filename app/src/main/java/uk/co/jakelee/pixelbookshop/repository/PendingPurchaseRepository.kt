package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PendingPurchaseDao
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

class PendingPurchaseRepository(private val pendingPurchaseDao: PendingPurchaseDao) {

    suspend fun addPurchase(vararg pendingPurchase: PendingPurchase) = pendingPurchaseDao.insert(*pendingPurchase)

    suspend fun deletePurchase(vararg pendingPurchase: PendingPurchase) = pendingPurchaseDao.deletePendingPurchase(*pendingPurchase)

    suspend fun deletePurchase() = pendingPurchaseDao.deletePendingPurchases()
}