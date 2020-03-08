package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.PastVisitDao
import uk.co.jakelee.pixelbookshop.database.entity.PastVisit
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class PastVisitRepository(private val pastVisitDao: PastVisitDao) {

    suspend fun addVisit(pastVisit: PastVisit) = pastVisitDao.insert(pastVisit)

    fun getRecentVisits() = pastVisitDao.getLatestVisits()

    fun getRecentVisitsByVisitor(visitor: Visitor) = pastVisitDao.getLatestVisitsByVisitor(visitor)

    suspend fun tidyUpVisits() = pastVisitDao.deleteAllOlderVisits()
}