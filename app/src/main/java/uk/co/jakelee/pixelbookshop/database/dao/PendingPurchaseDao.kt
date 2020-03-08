package uk.co.jakelee.pixelbookshop.database.dao

import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.PendingPurchase

@Dao
interface PendingPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pendingPurchase: PendingPurchase)

    @Delete
    fun deletePendingPurchase(vararg pendingPurchase: PendingPurchase)

    @Query("DELETE FROM PendingPurchase")
    fun deletePendingPurchases()
}