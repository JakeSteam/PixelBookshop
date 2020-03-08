package uk.co.jakelee.pixelbookshop.database.dao

import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

@Dao
interface PendingPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pendingPurchases: List<PendingPurchase>)

    @Delete
    fun deletePendingPurchases(pendingPurchase: List<PendingPurchase>)

    @Query("DELETE FROM PendingPurchase")
    fun deletePendingPurchases()
}