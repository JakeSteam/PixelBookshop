package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val playerRepo: PlayerRepository

    init {
        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is shop Fragment"
    }
    val text: LiveData<String> = _text

    fun addXp(xp: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playerRepo.addXp(xp)
        }
    }
}