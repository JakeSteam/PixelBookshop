package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PendingVisitDao
import uk.co.jakelee.pixelbookshop.database.entity.PendingVisit

class PendingVisitRepository(private val pendingVisitDao: PendingVisitDao) {

    suspend fun addVisit(vararg pendingVisit: PendingVisit) = pendingVisitDao.insert(*pendingVisit)

    suspend fun deleteVisit(vararg pendingVisit: PendingVisit) = pendingVisitDao.deleteVisit(*pendingVisit)

    suspend fun deleteVisits() = pendingVisitDao.deleteAllVisits()
}