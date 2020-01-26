package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Player
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Wall

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

    @Query("SELECT wall, isX, position FROM player")
    fun getWall(): LiveData<WallInfo>

    @Query("SELECT started FROM player")
    fun getTimeStarted(): LiveData<Long>

    @Query("UPDATE player SET xp = (xp + :xp)")
    fun addXp(xp: Int)

    @Query("UPDATE player SET wall = :wall")
    fun setWall(wall: Wall)

}