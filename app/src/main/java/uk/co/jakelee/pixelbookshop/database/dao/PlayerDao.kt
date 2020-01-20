package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Player

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Query("SELECT name FROM player")
    fun getName(): LiveData<String>

    @Query("SELECT xp FROM player")
    fun getXp(): LiveData<Long>

    @Query("SELECT coins FROM player")
    fun getCoins(): LiveData<Long>

    @Query("SELECT started FROM player")
    fun getTimeStarted(): LiveData<Long>

    @Query("UPDATE player SET xp = (xp + :xp)")
    fun addXp(xp: Int)

}