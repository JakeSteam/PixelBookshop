package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks

@Dao
interface OwnedFurnitureDao {
    @Query("SELECT * FROM OwnedFurniture WHERE x = :x AND y = :y")
    fun getByPosition(x: Int, y: Int): OwnedFurniture?

    @Query("SELECT * FROM OwnedFurniture ORDER BY x ASC, y DESC")
    fun getAll(): LiveData<List<OwnedFurniture>>

    @Transaction
    @Query("SELECT * FROM OwnedFurniture ORDER BY x ASC, y DESC")
    fun getAllWithBooks(): LiveData<List<OwnedFurnitureWithOwnedBooks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg ownedFurniture: OwnedFurniture)

    @Query("UPDATE OwnedFurniture SET x = x + 1 WHERE shopId = :shopId")
    suspend fun increaseX(shopId: Int)

    @Query("UPDATE OwnedFurniture SET y = y + 1 WHERE shopId = :shopId")
    suspend fun increaseY(shopId: Int)

    @Query("UPDATE OwnedBook SET ownedFurnitureId = :furniId WHERE id IN(:bookIds)")
    suspend fun assignBooks(furniId: Int, bookIds: List<Int>)
}