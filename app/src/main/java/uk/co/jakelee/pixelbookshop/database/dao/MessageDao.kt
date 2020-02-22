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

    @Query("SELECT * FROM message ORDER BY TIME DESC LIMIT :limit")
    fun getRecentMessages(limit: Int = 100): LiveData<List<Message>>

    @Query("UPDATE message SET dismissed = 1 WHERE id = :id")
    fun dismissMessage(id: Int)

    @Query("UPDATE message SET dismissed = 1")
    fun dismissAllMessages()

    @Query("DELETE FROM message WHERE id NOT IN (SELECT id FROM message ORDER BY time DESC LIMIT 100)")
    fun deleteAllOlderMessages()
}