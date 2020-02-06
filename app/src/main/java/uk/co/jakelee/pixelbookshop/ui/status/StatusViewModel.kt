package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.lookups.FurnitureType
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository


class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val ownedBookRepo: OwnedBookRepository
    private val playerRepo: PlayerRepository

    val storageInfo: LiveData<Pair<Int, Int>>
    val name: LiveData<String>
    val xp: LiveData<Long>

    data class CoinData(var coins: Int?, var max: Int?) {
        fun isValid() = coins != null && max != null
    }

    data class BookData(var books: Int?, var max: Int?) {
        fun isValid() = books != null && max != null
    }

    init {
        val ownedFurnitureDao = AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        val ownedBookDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        ownedBookRepo = OwnedBookRepository(ownedBookDao)
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
    }

    fun getBookData(): MediatorLiveData<BookData> {
        val mediatorLiveData = MediatorLiveData<BookData>()
        val current = BookData(null, null)
        mediatorLiveData.addSource(ownedBookRepo.allBooks) { books ->
            current.books = books.count { it.ownedFurnitureId == null }
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(ownedFurnitureRepo.allFurniture) { furniture ->
            current.max = furniture.sumBy { it.furniture.capacity }
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }

    fun getCoinData(): MediatorLiveData<CoinData> {
        val mediatorLiveData = MediatorLiveData<CoinData>()
        val current = CoinData(null, null)
        mediatorLiveData.addSource(playerRepo.coins) { coins ->
            current.coins = coins.toInt()
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(ownedFurnitureRepo.allFurniture) { furniture ->
            current.max = furniture.sumBy { it.furniture.capacity * 100 }
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
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