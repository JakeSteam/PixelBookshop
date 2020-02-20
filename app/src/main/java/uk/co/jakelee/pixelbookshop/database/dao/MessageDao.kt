package uk.co.jakelee.pixelbookshop.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.co.jakelee.pixelbookshop.database.entity.Message

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Query("SELECT * FROM message ORDER BY time DESC LIMIT 1")
    fun getLatestMessage(): LiveData<Message>

    @Query("SELECT * FROM message")
    fun getAllMessages(): LiveData<List<Message>>

    @Query("UPDATE message SET dismissed = 1 WHERE id = :id")
    fun dismissMessage(id: Int)

}