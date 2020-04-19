package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.dto.GameTime
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.lookups.bookCapacity
import uk.co.jakelee.pixelbookshop.lookups.coinCapacity
import uk.co.jakelee.pixelbookshop.repository.*
import uk.co.jakelee.pixelbookshop.utils.PendingPurchaseGenerator


class StatusViewModel(application: Application) : AndroidViewModel(application) {

    private val pendingPurchaseRepo: PendingPurchaseRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val ownedBookRepo: OwnedBookRepository
    private val playerRepo: PlayerRepository
    private val messageRepo: MessageRepository

    val ownedFurniture: LiveData<List<OwnedFurnitureWithOwnedBooks>>
    val date: LiveData<GameTime>
    val xp: LiveData<Long>
    val messages: LiveData<List<Message>>
    val isPlaying = MutableLiveData(false)
    private var currentTimer: Job? = null

    data class CoinData(var coins: Int?, var max: Int?) {
        fun isValid() = coins != null && max != null
    }

    data class BookData(var assignedBooks: Int?, var unassignedBooks: Int?, var maxUnassignedBooks: Int?) {
        fun isValid() = assignedBooks != null && unassignedBooks != null && maxUnassignedBooks != null
    }

    init {
        val pendingPurchaseDao = AppDatabase.getDatabase(application, viewModelScope).pendingPurchaseDao()
        pendingPurchaseRepo = PendingPurchaseRepository(pendingPurchaseDao, 0)

        val ownedFurnitureDao = AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao, 0)
        ownedFurniture = ownedFurnitureRepo.allFurnitureWithBooks

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

    fun toggleTime() = isPlaying.value?.let { shouldPause ->
        isPlaying.value = !shouldPause
        if (shouldPause) stopTime() else startTime()
    }

    private fun startTime() {
        isPlaying.value = true
        currentTimer = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (date.value!!.hour == 0) {
                    val todaysPurchases = PendingPurchaseGenerator().generate(date.value!!.day, ownedFurniture.value!!)
                    pendingPurchaseRepo.addPurchases(todaysPurchases)
                }
                repeat(99) {
                    tickTime()
                    delay(1000)
                }
            }
        }
    }

    private suspend fun tickTime() {
        if (date.value!!.hour < 9) {
            playerRepo.addHour()
        } else {
            playerRepo.nextDay()
            withContext(Dispatchers.Main) {
                stopTime()
            }
        }
    }

    private fun stopTime() {
        isPlaying.value = false
        currentTimer?.cancel()
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