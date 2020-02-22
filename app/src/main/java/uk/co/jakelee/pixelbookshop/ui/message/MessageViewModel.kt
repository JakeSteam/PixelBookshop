package uk.co.jakelee.pixelbookshop.ui.message

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.repository.MessageRepository

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val messageRepo: MessageRepository

    val messages: LiveData<List<Message>>

    init {
        val messageDao = AppDatabase.getDatabase(application, viewModelScope).messageDao()
        messageRepo = MessageRepository(messageDao)
        messages = messageRepo.getRecentMessages()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messageRepo.tidyUpMessages()
            }
        }
    }
}