package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        val ownedBookDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        ownedBookRepo = OwnedBookRepository(ownedBookDao)
        ownedBookCount = ownedBookRepo.bookCount

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        name = playerRepo.name
        xp = playerRepo.xp
        coins = playerRepo.coins
    }

    fun insert(ownedBook: OwnedBook) = viewModelScope.launch {
       ownedBookRepo.insert(ownedBook)
    }

    fun addXp(xp: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playerRepo.addXp(xp)
        }
    }
}