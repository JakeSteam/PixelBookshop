package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Shop
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Wall

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shop: Shop)

    @Query("SELECT wall, isDoorOnX, doorPosition FROM shop WHERE id = :shopId")
    fun getWall(shopId: Int): LiveData<WallInfo>

    @Query("UPDATE shop SET wall = :wall WHERE id = :shopId")
    fun setWall(wall: Wall, shopId: Int)

    @Query("UPDATE shop SET isDoorOnX = :isDoorOnX, doorPosition = :position WHERE id = :shopId")
    fun setDoor(isDoorOnX: Boolean, position: Int, shopId: Int)

}