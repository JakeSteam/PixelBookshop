package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.dao.OwnedBookDao
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class OwnedBookRepository(private val ownedBookDao: OwnedBookDao) {

    val bookCount: LiveData<Long> = ownedBookDao.getBookCount()

    val allBooks: LiveData<List<OwnedBook>> = ownedBookDao.getAll()

    suspend fun insert(ownedBook: OwnedBook) {
        ownedBookDao.insert(ownedBook)
    }
}