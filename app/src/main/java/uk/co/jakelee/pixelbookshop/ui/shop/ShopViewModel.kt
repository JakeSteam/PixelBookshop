package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.dto.GameTime
import uk.co.jakelee.pixelbookshop.extensions.getSatisfaction
import uk.co.jakelee.pixelbookshop.extensions.toCurrencyString
import uk.co.jakelee.pixelbookshop.lookups.*
import uk.co.jakelee.pixelbookshop.repository.*
import java.math.BigDecimal


class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val shopId = 1

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val ownedBookRepo: OwnedBookRepository
    private val shopRepo: ShopRepository
    private val pastPurchaseRepo: PastPurchaseRepository
    private val pendingPurchaseRepo: PendingPurchaseRepository
    private val playerRepo: PlayerRepository
    private val messageRepo: MessageRepository

    private val ownedFloor: LiveData<List<OwnedFloor>>
    val ownedFurniture: LiveData<List<OwnedFurnitureWithOwnedBooks>>
    private val wall: LiveData<WallInfo>
    val pendingPurchases: LiveData<List<PendingPurchase>>
    private val dateTime: LiveData<GameTime>

    var latestMessage: LiveData<Message>

    private var selectedFurni: OwnedFurniture? = null

    var booksToAssign = arrayOf<Int>()
    var currentTab = MutableLiveData(ShopFragment.SelectedTab.NONE)

    init {
        val ownedFloorDao = AppDatabase.getDatabase(application, viewModelScope).ownedFloorDao()
        ownedFloorRepo = OwnedFloorRepository(ownedFloorDao)
        ownedFloor = ownedFloorRepo.allFloor

        val ownedFurnitureDao = AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        ownedFurniture = ownedFurnitureRepo.allFurnitureWithBooks

        val ownedBookDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        ownedBookRepo = OwnedBookRepository(ownedBookDao)

        val shopDao = AppDatabase.getDatabase(application, viewModelScope).shopDao()
        shopRepo = ShopRepository(shopDao, shopId)
        wall = shopRepo.wall

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        dateTime = playerRepo.date

        val pastPurchaseDao = AppDatabase.getDatabase(application, viewModelScope).pastPurchaseDao()
        pastPurchaseRepo = PastPurchaseRepository(pastPurchaseDao)

        val pendingPurchaseDao = AppDatabase.getDatabase(application, viewModelScope).pendingPurchaseDao()
        pendingPurchaseRepo = PendingPurchaseRepository(pendingPurchaseDao)
        pendingPurchases = pendingPurchaseRepo.allPurchases

        val messageDao = AppDatabase.getDatabase(application, viewModelScope).messageDao()
        messageRepo = MessageRepository(messageDao)
        latestMessage = messageRepo.latestMessage()
    }

    var visitorCheckedOut = Triple(0, 0, 0)
    var lastTickReturned = 0
    fun getTickData(): LiveData<ShopUiUpdate> = Transformations.map(dateTime) { data ->
        if (data == null) {
            Log.i("VisitorPosition", "Null data, uh oh")
            return@map ShopUiUpdate(GameTime(0, 0), emptyList())
        } else if (data.hour == 0) {
            Log.i("VisitorPosition", "It's hour 0, nothing to do")
            return@map ShopUiUpdate(data, emptyList())
        } else if (lastTickReturned == data.hour) {
            Log.i("VisitorPosition", "Er, we've already processed this hour")
            return@map ShopUiUpdate(data, emptyList())
        }
        setOrResetMode(ShopFragment.SelectedTab.NONE)
        val visitorLocations = pendingPurchaseRepo.allPurchases.value!!
            .groupBy { it.visitor }
            .map  { purchasesByVisitor ->
                // If currently picking up an item, need to be at that display
                val purchaseThisTick = purchasesByVisitor.value.findLast { it.day == data.day && it.hour == data.hour }
                if (purchaseThisTick != null) {
                    val purchaseFurniture = ownedFurniture.value!!.first {
                        it.ownedBooks.firstOrNull {  ownedBook ->
                            ownedBook.id == purchaseThisTick.ownedBookId
                        } != null
                    }

                    viewModelScope.launch { withContext(Dispatchers.IO) {
                        messageRepo.addMessage(MessageType.Positive, String.format(getString(R.string.message_visitor_picked_up), purchasesByVisitor.key.name, purchaseThisTick.ownedBookId))
                    } }

                    Log.i("VisitorPosition", "Purchasing on tick ${data.hour}")
                    return@map Pair(purchaseThisTick.visitor, purchaseFurniture.ownedFurniture)
                }

                // If finished purchasing, need to be sitting down
                val allPurchasesCompleted = purchasesByVisitor.value.all { it.day == data.day && it.hour < data.hour }
                if (allPurchasesCompleted && visitorCheckedOut != Triple(purchasesByVisitor.key.id, data.day, data.hour)) {
                    visitorCheckedOut = Triple(purchasesByVisitor.key.id, data.day, data.hour)
                    viewModelScope.launch { withContext(Dispatchers.IO) {
                            performCheckout(purchasesByVisitor.key, purchasesByVisitor.value)
                    } }
                    val seatingArea = ownedFurniture.value!!.first { it.ownedFurniture.id == purchasesByVisitor.value.first().seatingAreaId }
                    Log.i("VisitorPosition", "Sitting down (to checkout) on tick ${data.hour}")
                    return@map Pair(purchasesByVisitor.key,  seatingArea.ownedFurniture)
                }

                // If holding items but not checking out, also sit down
                val isIdlingInStore = purchasesByVisitor.value.firstOrNull { it.day == data.day && it.hour < data.hour } != null
                if (isIdlingInStore && visitorCheckedOut != Triple(purchasesByVisitor.key.id, data.day, data.hour)) {
                    val seatingArea = ownedFurniture.value!!.first { it.ownedFurniture.id == purchasesByVisitor.value.first().seatingAreaId }
                    Log.i("VisitorPosition", "Sitting down on tick ${data.hour}")
                    return@map Pair(purchasesByVisitor.key, seatingArea.ownedFurniture)
                }

                // Otherwise not in store
                return@map Pair(purchasesByVisitor.key, null)
            }
        Log.i("VisitorPosition", "Returning all locations on tick ${data.hour}")
        lastTickReturned = data.hour
        return@map ShopUiUpdate(data, visitorLocations)
    }

    private suspend fun performCheckout(visitor: Visitor, pendingPurchases: List<PendingPurchase>) {
        val books = mutableListOf<OwnedBook>()
        var totalCost = BigDecimal(0)
        val pastPurchases = pendingPurchases.mapNotNull {
            ownedBookRepo.getBook(it.ownedBookId)?.let { book ->
                books.add(book)
                val satisfaction = it.visitor.getSatisfaction(book)
                totalCost = totalCost.plus(satisfaction.bookValue)
                it.toPastPurchase(book, satisfaction.satisfaction)
            }
        }

        // Visitor purchases books
        if (books.isNotEmpty()) {
            playerRepo.addCoins(totalCost)
            playerRepo.addXp(totalCost.toInt())
            pendingPurchaseRepo.deletePurchase(pendingPurchases)
            pastPurchaseRepo.addPurchases(pastPurchases)
            ownedBookRepo.delete(books)
            messageRepo.addMessage(
                MessageType.Positive,
                String.format(getString(R.string.message_visitor_purchased), visitor.name, pendingPurchases.size, totalCost.toCurrencyString())
            )
        } else {
            messageRepo.addMessage(MessageType.Negative, getString(R.string.message_visitor_no_purchases))
        }
        Log.i("VisitorPosition", "Finished checking out")
    }

    fun addStrip(isPositive: Boolean, isX: Boolean) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            ownedFloor.value?.let { floors ->
                val maxX = floors.last().x
                val maxY = floors.first().y
                val max = if (isX) maxY else maxX
                val list: MutableList<OwnedFloor> = mutableListOf()
                for (i in 0..max) {
                    val x = if (isX) (if (isPositive) maxX + 1 else 0) else i
                    val y = if (isX) i else (if (isPositive) maxY + 1 else 0)
                    list.add(OwnedFloor(shopId, x, y, false, Floor.Dirt))
                }
                if (!isPositive) {
                    list.addAll(floors.apply { forEach {
                        if (isX) it.x++ else it.y++
                    } })
                }
                ownedFloorRepo.insert(list)
                if (!isPositive) {
                    updatesTablesAfterNegativeStrip(isX, maxY)
                }
            }
        }
    }

    private suspend fun updatesTablesAfterNegativeStrip(isX: Boolean, maxY: Int) {
        shopRepo.wall.value?.let {
            if (isX && it.isDoorOnX) {
                shopRepo.setDoor(it.doorPosition + 1, maxY, shopId)
            } else if (!isX && !it.isDoorOnX) {
                shopRepo.setDoor(0, it.doorPosition + 1, shopId)
            }
        }
        ownedFurnitureRepo.apply { if (isX) increaseX(shopId) else increaseY(shopId) }
    }

    fun markMessageAsDismissed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            latestMessage.value?.let {
                messageRepo.dismissMessage(it.id)
            }
        }
    }

    fun setOrResetMode(selectedTab: ShopFragment.SelectedTab) {
        if (currentTab.value == selectedTab) {
            currentTab.value = ShopFragment.SelectedTab.NONE
        } else {
            currentTab.value = selectedTab
        }
    }

    fun getShopData(): MediatorLiveData<ShopData> {
        val mediatorLiveData = MediatorLiveData<ShopData>()
        val current = ShopData()
        mediatorLiveData.addSource(wall) { list ->
            current.wall = list
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(ownedFloor) { list ->
            current.floors = list
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(ownedFurniture) { list ->
            current.furnitures = list
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }

    private fun getString(@StringRes stringId: Int) = getApplication<Application>().resources.getString(stringId)

    fun wallClick(wall: WallInfo, x: Int, y: Int, shopId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.MOVE -> ownedFurniture.value?.let { furnitures ->
                    if (!furnitures.any { it.ownedFurniture.x == x && it.ownedFurniture.y == y }) {
                        shopRepo.setDoor(x, y, shopId)
                    }
                }
                ShopFragment.SelectedTab.UPGRADE -> wall.wall.also {
                    val nextWall = Wall.values().firstOrNull { wall -> wall.tier > it.tier }
                    if (nextWall != null && playerRepo.canPurchase(nextWall.cost, 0)) {
                        shopRepo.upgradeWall(nextWall, shopId)
                        playerRepo.purchase(nextWall.cost)
                        getApplication<Application>().resources
                        messageRepo.addMessage(MessageType.Positive, String.format(getString(R.string.message_item_purchased), getString(nextWall.title)))
                    } else if (nextWall != null) {
                        messageRepo.addMessage(MessageType.Negative, String.format(getString(R.string.message_cannot_afford_upgrade), nextWall.cost))
                    } else {
                        messageRepo.addMessage(MessageType.Neutral, getString(R.string.message_cannot_upgrade_further))
                    }
                }
                else -> null
            }
        }
    }

    fun floorClick(floor: OwnedFloor) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> floor.floor?.let {
                    val nextFloor = Floor.values().firstOrNull { floor -> floor.tier > it.tier }
                    if (nextFloor != null && playerRepo.canPurchase(nextFloor.cost, 0)) {
                        ownedFloorRepo.upgradeFloor(floor, nextFloor)
                        playerRepo.purchase(nextFloor.cost)
                        messageRepo.addMessage(MessageType.Positive, String.format(getString(R.string.message_item_purchased), getString(nextFloor.title)))
                    } else if (nextFloor != null) {
                        messageRepo.addMessage(MessageType.Negative, String.format(getString(R.string.message_cannot_afford_upgrade), nextFloor.cost))
                    } else {
                        messageRepo.addMessage(MessageType.Neutral, getString(R.string.message_cannot_upgrade_further))
                    }
                }
                ShopFragment.SelectedTab.ROTATE -> ownedFloorRepo.rotateFloor(floor)
                ShopFragment.SelectedTab.MOVE -> wall.value?.let {
                    if (!it.isDoor(floor.x, floor.y, ownedFloor.value!!.first().y)) {
                        ownedFurnitureRepo.moveFurni(floor, selectedFurni)
                        selectedFurni = null
                    }
                }
                else -> null
            }
        }
    }

    fun furniClick(furni: OwnedFurnitureWithOwnedBooks) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> furni.ownedFurniture.furniture.also {
                    val nextFurni = Furniture.values().firstOrNull { furni ->
                        furni.type == it.type && furni.tier > it.tier
                    }
                    if (nextFurni != null && playerRepo.canPurchase(nextFurni)) {
                        ownedFurnitureRepo.upgradeFurni(furni.ownedFurniture, nextFurni)
                        playerRepo.purchase(nextFurni.cost)
                        messageRepo.addMessage(MessageType.Positive, String.format(getString(R.string.message_item_purchased), getString(nextFurni.title)))
                    } else if (nextFurni != null) {
                        messageRepo.addMessage(MessageType.Negative, String.format(getString(R.string.message_cannot_afford_upgrade), nextFurni.cost))
                    } else {
                        messageRepo.addMessage(MessageType.Neutral, getString(R.string.message_cannot_upgrade_further))
                    }
                }
                ShopFragment.SelectedTab.ROTATE -> ownedFurnitureRepo.rotateFurni(furni.ownedFurniture)
                ShopFragment.SelectedTab.MOVE -> wall.value?.let {
                    if (selectedFurni == null) {
                        selectedFurni = furni.ownedFurniture
                    } else if (!it.isDoor(furni.ownedFurniture.x, furni.ownedFurniture.y, ownedFloor.value!!.first().y)) {
                        ownedFurnitureRepo.swap(furni.ownedFurniture, selectedFurni!!)
                        selectedFurni = null
                    }
                }
                ShopFragment.SelectedTab.ASSIGN -> {
                    if (furni.ownedFurniture.furniture.type != FurnitureType.Display) {
                        messageRepo.addMessage(MessageType.Negative, getString(R.string.message_books_only_in_display_furniture))
                        return@withContext
                    }
                    val furnitureCapacity = furni.ownedFurniture.furniture.bookCapacity()
                    val capacity = furnitureCapacity - furni.ownedBooks.size
                    val slicedBooksToAssign = booksToAssign.take(capacity)
                    if (capacity == 0) {
                        messageRepo.addMessage(MessageType.Negative, String.format(getString(R.string.message_display_furniture_at_capacity), furnitureCapacity))
                        return@withContext
                    }
                    ownedFurnitureRepo.assignBooks(furni.ownedFurniture.id, slicedBooksToAssign)
                    if (slicedBooksToAssign.size < booksToAssign.size) {
                        messageRepo.addMessage(MessageType.Neutral, "There was only space for ${slicedBooksToAssign.size}/${booksToAssign.size} books, they've been assigned!")
                    } else {
                        messageRepo.addMessage(MessageType.Positive, "${slicedBooksToAssign.size} books assigned!")
                    }
                    booksToAssign = arrayOf()
                    launch(Dispatchers.Main) { setOrResetMode(ShopFragment.SelectedTab.NONE) }
                }
                else -> null
            }
        }
    }
}