package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.OwnedBookDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class OwnedBookRepository(private val ownedBookDao: OwnedBookDao) {

    val bookCount = ownedBookDao.getBookCount()

    val allBooks = ownedBookDao.getAll()

    suspend fun insert(ownedBook: OwnedBook) {
        ownedBookDao.insert(ownedBook)
    }
}