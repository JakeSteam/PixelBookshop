package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor

@Dao
interface OwnedFloorDao {

    @Query("SELECT * FROM OwnedFloor ORDER BY x ASC, y DESC")
    fun getAll(): LiveData<List<OwnedFloor>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg ownedFloor: OwnedFloor)
}