package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

@Dao
interface OwnedBookDao {
    @Query("SELECT COUNT(*) FROM OwnedBook")
    fun getBookCount(): LiveData<Long>

    @Query("SELECT * FROM OwnedBook")
    fun getAll(): LiveData<List<OwnedBook>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg ownedBook: OwnedBook)

    @Query("DELETE FROM OwnedBook")
    suspend fun deleteAll()
}