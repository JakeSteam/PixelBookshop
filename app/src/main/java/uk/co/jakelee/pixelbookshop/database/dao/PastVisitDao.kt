package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.PastVisit
import uk.co.jakelee.pixelbookshop.lookups.Visitor

@Dao
interface PastVisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pastVisit: PastVisit)

    @Query("SELECT * FROM pastvisit ORDER BY day DESC, time DESC LIMIT :limit")
    fun getLatestVisits(limit: Int = 100): LiveData<List<PastVisit>>

    @Query("SELECT * FROM pastvisit WHERE visitor = :visitor ORDER BY day DESC, time DESC LIMIT :limit")
    fun getLatestVisitsByVisitor(visitor: Visitor, limit: Int = 100): LiveData<List<PastVisit>>

    @Query("DELETE FROM pastvisit WHERE id NOT IN (SELECT id FROM pastvisit ORDER BY day DESC, time DESC LIMIT 100)")
    fun deleteAllOlderVisits()
}