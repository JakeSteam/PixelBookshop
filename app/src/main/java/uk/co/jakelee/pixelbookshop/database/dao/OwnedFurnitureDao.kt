package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks

@Dao
interface OwnedFurnitureDao {

    @Query("SELECT * FROM OwnedFurniture ORDER BY y, x")
    fun getAll(): LiveData<List<OwnedFurniture>>

    @Transaction
    @Query("SELECT * FROM OwnedFurniture ORDER BY y, x")
    fun getAllWithBooks(): LiveData<List<OwnedFurnitureWithOwnedBooks>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg ownedFurniture: OwnedFurniture)
}