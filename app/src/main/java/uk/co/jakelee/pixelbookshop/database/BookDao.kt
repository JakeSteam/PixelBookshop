package uk.co.jakelee.pixelbookshop.database

import androidx.room.Dao
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id = :bookId LIMIT 1")
    fun getOne(bookId: Int): Book

    @Query("UPDATE book SET owned = (owned + 1) WHERE id = :bookId")
    fun addOne(bookId: Int): Book
}