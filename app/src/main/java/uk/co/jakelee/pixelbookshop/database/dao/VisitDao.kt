package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.database.entity.Visit
import uk.co.jakelee.pixelbookshop.lookups.Visitor

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: Visit)

    @Query("SELECT * FROM visit ORDER BY day DESC, time DESC LIMIT :limit")
    fun getLatestVisits(limit: Int = 100): LiveData<List<Visit>>

    @Query("SELECT * FROM visit WHERE visitor = :visitor ORDER BY day DESC, time DESC LIMIT :limit")
    fun getLatestVisitsByVisitor(visitor: Visitor, limit: Int = 100): LiveData<List<Visit>>

    @Query("DELETE FROM visit WHERE id NOT IN (SELECT id FROM visit ORDER BY day DESC, time DESC LIMIT 100)")
    fun deleteAllOlderVisits()
}