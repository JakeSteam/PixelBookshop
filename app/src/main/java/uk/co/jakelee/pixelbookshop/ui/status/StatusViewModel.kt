package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedBookRepo: OwnedBookRepository
    private val playerRepo: PlayerRepository

    val ownedBookCount: LiveData<Long>
    val name: LiveData<String>
    val xp: LiveData<Long>
    val coins: LiveData<Long>
    val timeStarted: LiveData<Long>

    init {
        val ownedBookDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        ownedBookRepo = OwnedBookRepository(ownedBookDao)
        ownedBookCount = ownedBookRepo.bookCount

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        name = playerRepo.name
        xp = playerRepo.xp
        coins = playerRepo.coins
        timeStarted = playerRepo.timeStarted
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(ownedBook: OwnedBook) = viewModelScope.launch {
       ownedBookRepo.insert(ownedBook)
    }
}