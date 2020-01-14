package uk.co.jakelee.pixelbookshop.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Query("DELETE FROM book")
    suspend fun deleteAll()
}