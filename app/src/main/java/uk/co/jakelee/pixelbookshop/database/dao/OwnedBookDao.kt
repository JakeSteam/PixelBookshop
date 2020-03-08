package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

@Dao
interface OwnedBookDao {
    @Query("SELECT COUNT(*) FROM OwnedBook")
    fun getBookCount(): LiveData<Long>

    @Query("SELECT * FROM OwnedBook")
    fun getAll(): LiveData<List<OwnedBook>>

    @Query("SELECT * FROM OwnedBook WHERE id = :ownedBookId")
    fun get(ownedBookId: Int): OwnedBook

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg ownedBook: OwnedBook)

    @Delete
    fun delete(ownedBooks: List<OwnedBook>)

    @Query("DELETE FROM OwnedBook")
    suspend fun deleteAll()
}