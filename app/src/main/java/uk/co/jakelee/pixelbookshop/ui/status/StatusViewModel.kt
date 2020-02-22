package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao.PlayerDate
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.lookups.bookCapacity
import uk.co.jakelee.pixelbookshop.lookups.coinCapacity
import uk.co.jakelee.pixelbookshop.repository.MessageRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository


class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val ownedBookRepo: OwnedBookRepository
    private val playerRepo: PlayerRepository
    private val messageRepo: MessageRepository

    val date: LiveData<PlayerDate>
    val xp: LiveData<Long>
    val messages: LiveData<List<Message>>

    data class CoinData(var coins: Int?, var max: Int?) {
        fun isValid() = coins != null && max != null
    }

    data class BookData(var assignedBooks: Int?, var unassignedBooks: Int?, var maxUnassignedBooks: Int?) {
        fun isValid() = assignedBooks != null && unassignedBooks != null && maxUnassignedBooks != null
    }

    init {
        val ownedFurnitureDao = AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        val ownedBookDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        ownedBookRepo = OwnedBookRepository(ownedBookDao)
        val messageDao = AppDatabase.getDatabase(application, viewModelScope).messageDao()
        messageRepo = MessageRepository(messageDao)
        messages = messageRepo.getRecentMessages()

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        date = playerRepo.date
        xp = playerRepo.xp
    }

    fun getBookData(): MediatorLiveData<BookData> {
        val mediatorLiveData = MediatorLiveData<BookData>()
        val current = BookData(null, null, null)
        mediatorLiveData.addSource(ownedBookRepo.allBooks) { books ->
            // Gets all books not in / in furniture
            current.unassignedBooks = books.count { it.ownedFurnitureId == null }
            current.assignedBooks = books.size - current.unassignedBooks!!
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(ownedFurnitureRepo.allFurniture) { furniture ->
            // Gets total capacity for undisplayed books
            current.maxUnassignedBooks = furniture.sumBy { it.furniture.bookCapacity() }
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }

    fun getCoinData(): MediatorLiveData<CoinData> {
        val mediatorLiveData = MediatorLiveData<CoinData>()
        val current = CoinData(null, null)
        mediatorLiveData.addSource(playerRepo.coins) {
            it?.let { coins ->
                current.coins = coins.toInt()
                mediatorLiveData.value = current
            }
        }
        mediatorLiveData.addSource(ownedFurnitureRepo.allFurniture) {
            it?.let { furnitures ->
                current.max = furnitures.sumBy { furniture -> furniture.furniture.coinCapacity() }
                mediatorLiveData.value = current
            }
        }
        return mediatorLiveData
    }

}