package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture

@Dao
interface OwnedFurnitureDao {

    @Query("SELECT * FROM OwnedFurniture ORDER BY y, x")
    fun getAll(): LiveData<List<OwnedFurniture>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg ownedFurniture: OwnedFurniture)
}