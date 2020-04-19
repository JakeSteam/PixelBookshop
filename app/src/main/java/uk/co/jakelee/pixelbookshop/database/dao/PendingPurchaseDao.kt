package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

@Dao
interface PendingPurchaseDao {

    @Query("SELECT * FROM PendingPurchase")
    fun getAll(): LiveData<List<PendingPurchase>>

    @Query("SELECT * FROM PendingPurchase WHERE shopId = :shopId")
    fun get(shopId: Int): LiveData<List<PendingPurchase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pendingPurchases: List<PendingPurchase>)

    @Delete
    fun deletePendingPurchases(pendingPurchase: List<PendingPurchase>)

    @Query("DELETE FROM PendingPurchase")
    fun deletePendingPurchases()
}