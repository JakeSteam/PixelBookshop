package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Player
import java.math.BigDecimal

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Query("SELECT * FROM player")
    fun getPlayer(): LiveData<Player>

    @Query("SELECT name FROM player")
    fun getName(): LiveData<String>

    @Query("SELECT xp FROM player")
    fun getXp(): LiveData<Long>

    @Query("UPDATE player SET xp = (xp + :xp)")
    fun addXp(xp: Int)

    @Query("SELECT coins FROM player")
    fun getCoins(): LiveData<BigDecimal>

    @Query("UPDATE player SET coins = (coins - :coins)")
    fun removeCoins(coins: BigDecimal)

    @Query("UPDATE player SET coins = (coins + :coins)")
    fun addCoins(coins: BigDecimal)

    @Query("UPDATE player SET hour = (hour + 1)")
    fun addHour()

    @Query("UPDATE player SET day = (day + 1), hour = 0")
    fun nextDay()

    @Query("SELECT day, hour FROM player")
    fun getDateTime(): LiveData<GameTime>

    data class GameTime(val day: Int, val hour: Int)
}