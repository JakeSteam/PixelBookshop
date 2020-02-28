package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.MessageDao
import uk.co.jakelee.pixelbookshop.database.dao.VisitDao
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.database.entity.Visit
import uk.co.jakelee.pixelbookshop.lookups.MessageType
import uk.co.jakelee.pixelbookshop.lookups.Visitor

class VisitRepository(private val visitDao: VisitDao) {

    suspend fun addVisit(visit: Visit) = visitDao.insert(visit)

    fun getRecentVisits() = visitDao.getLatestVisits()

    fun getRecentVisitsByVisitor(visitor: Visitor) = visitDao.getLatestVisitsByVisitor(visitor)

    suspend fun tidyUpVisits() = visitDao.deleteAllOlderVisits()
}