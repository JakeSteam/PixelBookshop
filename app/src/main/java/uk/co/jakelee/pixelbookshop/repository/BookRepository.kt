package uk.co.jakelee.pixelbookshop.repository

import androidx.lifecycle.LiveData
import uk.co.jakelee.pixelbookshop.database.BookDao
import uk.co.jakelee.pixelbookshop.database.entity.Book

class BookRepository(private val bookDao: BookDao) {

    val allBooks: LiveData<List<Book>> = bookDao.getAll()

    suspend fun insert(book: Book) {
        bookDao.insert(book)
    }
}