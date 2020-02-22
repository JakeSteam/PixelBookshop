package uk.co.jakelee.pixelbookshop.repository

import uk.co.jakelee.pixelbookshop.database.dao.MessageDao
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.lookups.MessageType

class MessageRepository(private val messageDao: MessageDao) {

    suspend fun addMessage(type: MessageType, message: String) {
        messageDao.insert(Message(0, type, message, false, System.currentTimeMillis()))
    }

    fun getRecentMessages() = messageDao.getRecentMessages()

    fun latestMessage() = messageDao.getLatestMessage()

    fun dismissMessage(id: Int) = messageDao.dismissMessage(id)

    suspend fun tidyUpMessages() {
        messageDao.deleteAllOlderMessages()
        messageDao.dismissAllMessages()
    }
}