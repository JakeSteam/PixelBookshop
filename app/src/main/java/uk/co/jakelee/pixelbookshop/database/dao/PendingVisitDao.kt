package uk.co.jakelee.pixelbookshop.database.dao

import androidx.room.*
import uk.co.jakelee.pixelbookshop.database.entity.PendingVisit

@Dao
interface PendingVisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pendingVisit: PendingVisit)

    @Delete
    fun deleteVisit(vararg pendingVisit: PendingVisit)

    @Query("DELETE FROM pendingVisit")
    fun deleteAllVisits()
}