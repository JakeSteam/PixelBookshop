package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PendingPurchaseDao
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

class PendingPurchaseRepository(private val pendingPurchaseDao: PendingPurchaseDao) {

    val allPurchases = pendingPurchaseDao.getAll()

    suspend fun addPurchases(pendingPurchases: List<PendingPurchase>){
        pendingPurchaseDao.deletePendingPurchases()
        pendingPurchaseDao.insert(pendingPurchases)
    }

    suspend fun deletePurchase(pendingPurchases: List<PendingPurchase>) = pendingPurchaseDao.deletePendingPurchases(pendingPurchases)

    suspend fun deletePurchases() = pendingPurchaseDao.deletePendingPurchases()
}