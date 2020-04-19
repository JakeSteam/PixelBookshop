package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor

@Dao
interface OwnedFloorDao {

    @Query("SELECT * FROM OwnedFloor ORDER BY x ASC, y DESC")
    fun getAll(): LiveData<List<OwnedFloor>>

    @Query("SELECT * FROM OwnedFloor WHERE shopId = :shopId ORDER BY x ASC, y DESC")
    fun get(shopId: Int): LiveData<List<OwnedFloor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ownedFloor: OwnedFloor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ownedFloors: List<OwnedFloor>)

    @Update
    suspend fun update(ownedFloor: OwnedFloor)

}