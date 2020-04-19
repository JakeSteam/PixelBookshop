package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.PastPurchase
import uk.co.jakelee.pixelbookshop.lookups.Visitor

@Dao
interface PastPurchaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pastPurchase: List<PastPurchase>)

    @Query("SELECT * FROM pastpurchase ORDER BY day DESC, hour DESC LIMIT :limit")
    fun getLatestPurchases(limit: Int = 100): LiveData<List<PastPurchase>>

    @Query("SELECT * FROM pastpurchase WHERE shopId = :shopId ORDER BY day DESC, hour DESC LIMIT :limit")
    fun getLatestPurchases(shopId: Int, limit: Int = 100): LiveData<List<PastPurchase>>

    @Query("SELECT * FROM pastpurchase WHERE visitor = :visitor ORDER BY day DESC, hour DESC LIMIT :limit")
    fun getLatestPurchasesByVisitor(visitor: Visitor, limit: Int = 100): LiveData<List<PastPurchase>>

    @Query("SELECT * FROM pastpurchase WHERE shopId = :shopId AND visitor = :visitor ORDER BY day DESC, hour DESC LIMIT :limit")
    fun getLatestPurchasesByVisitor(shopId: Int, visitor: Visitor, limit: Int = 100): LiveData<List<PastPurchase>>

    @Query("DELETE FROM pastpurchase WHERE id NOT IN (SELECT id FROM pastpurchase ORDER BY day DESC, hour DESC LIMIT 100)")
    fun deleteAllOlderPurchases()
}