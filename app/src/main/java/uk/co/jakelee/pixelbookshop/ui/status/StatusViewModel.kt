package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.lookups.FurnitureType
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val playerRepo: PlayerRepository

    val storageInfo: LiveData<Pair<Int, Int>>
    val name: LiveData<String>
    val xp: LiveData<Long>
    val coins: LiveData<Long>

    init {
        val ownedFurnitureDao =
            AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        storageInfo = Transformations.map(ownedFurnitureRepo.allFurnitureWithBooks) { list ->
            var books = 0
            var storage = 0
            list.forEach {
                if (it.ownedFurniture.furniture.type == FurnitureType.Display) {
                    books += it.ownedBooks.size
                } else if (it.ownedFurniture.furniture.type == FurnitureType.Storage) {
                    storage += it.ownedFurniture.furniture.capacity
                }
            }
            Pair(books, storage)
        }

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        name = playerRepo.name
        xp = playerRepo.xp
        coins = playerRepo.coins
    }

    fun insert(ownedBook: OwnedBook) = viewModelScope.launch {
       //ownedBookRepo.insert(ownedBook)
    }

    fun addXp(xp: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playerRepo.addXp(xp)
        }
    }
}