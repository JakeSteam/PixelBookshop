package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.lookups.*
import uk.co.jakelee.pixelbookshop.repository.*
import uk.co.jakelee.pixelbookshop.utils.VisitGenerator

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val shopId = 1

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val shopRepo: ShopRepository
    private val pastVisitRepo: PastVisitRepository
    private val pendingVisitRepo: PendingVisitRepository
    private val playerRepo: PlayerRepository
    private val messageRepo: MessageRepository

    private val ownedFloor: LiveData<List<OwnedFloor>>
    private val ownedFurniture: LiveData<List<OwnedFurnitureWithOwnedBooks>>
    private val wall: LiveData<WallInfo>
    private val player: LiveData<Player>
    var latestMessage: LiveData<Message>

    private var selectedFurni: OwnedFurniture? = null

    var booksToAssign = arrayOf<Int>()
    var currentTab = MutableLiveData(ShopFragment.SelectedTab.NONE)

    init {
        val ownedFloorDao = AppDatabase.getDatabase(application, viewModelScope).ownedFloorDao()
        ownedFloorRepo = OwnedFloorRepository(ownedFloorDao)
        ownedFloor = ownedFloorRepo.allFloor

        val ownedFurnitureDao =
            AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        ownedFurniture = ownedFurnitureRepo.allFurnitureWithBooks

        val shopDao = AppDatabase.getDatabase(application, viewModelScope).shopDao()
        shopRepo = ShopRepository(shopDao, shopId)
        wall = shopRepo.wall

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        player = playerRepo.player

        val pastVisitDao = AppDatabase.getDatabase(application, viewModelScope).pastVisitDao()
        pastVisitRepo = PastVisitRepository(pastVisitDao)

        val pendingVisitDao = AppDatabase.getDatabase(application, viewModelScope).pendingVisitDao()
        pendingVisitRepo = PendingVisitRepository(pendingVisitDao)

        val messageDao = AppDatabase.getDatabase(application, viewModelScope).messageDao()
        messageRepo = MessageRepository(messageDao)
        latestMessage = messageRepo.latestMessage()
    }

    fun scheduleNextDay() {
        val todaysVisits = VisitGenerator().generate(player.value!!.day, ownedFurniture.value!!)
        // Save in database, clearing existing first (just in case). Need Dao etc first
        val a = 10
    }

    fun playNextDay() {
        // Fetch scheduled visits
        // If none already scheduled, run scheduler and wait for result. Otherwise use list.
        // For each, run through each visitor, with artificial delay.
        // When all steps finished, remove total gold, add xp, delete pending visit, add past visit, all as transaction
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
        mediatorLiveData.addSource(player) { player ->
            current.player = player
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }


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
                        messageRepo.addMessage(MessageType.Positive, "Purchased ${nextWall.title}!")
                    } else if (nextWall != null) {
                        messageRepo.addMessage(
                            MessageType.Negative,
                            "Can't afford upgrade, need ${nextWall.cost} coins!"
                        )
                    } else {
                        messageRepo.addMessage(
                            MessageType.Neutral,
                            "This wall is already fully upgraded!"
                        )
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
                        messageRepo.addMessage(
                            MessageType.Positive,
                            "Purchased ${nextFloor.title}!"
                        )
                    } else if (nextFloor != null) {
                        messageRepo.addMessage(
                            MessageType.Negative,
                            "Can't afford upgrade, need ${nextFloor.cost} coins!"
                        )
                    } else {
                        messageRepo.addMessage(
                            MessageType.Neutral,
                            "This floor tile is already fully upgraded!"
                        )
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
                    if (nextFurni != null && playerRepo.canPurchase(
                            nextFurni.cost,
                            nextFurni.level
                        )
                    ) {
                        ownedFurnitureRepo.upgradeFurni(furni.ownedFurniture, nextFurni)
                        playerRepo.purchase(nextFurni.cost)
                        messageRepo.addMessage(
                            MessageType.Positive,
                            "Purchased ${nextFurni.title}!"
                        )
                    } else if (nextFurni != null) {
                        messageRepo.addMessage(
                            MessageType.Negative,
                            "Can't afford upgrade, need ${nextFurni.cost} coins!"
                        )
                    } else {
                        messageRepo.addMessage(
                            MessageType.Neutral,
                            "This furniture is already fully upgraded!"
                        )
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
                        messageRepo.addMessage(MessageType.Negative, "Books can only be assigned to display furniture!")
                        return@withContext
                    }
                    val furnitureCapacity = furni.ownedFurniture.furniture.bookCapacity()
                    val capacity = furnitureCapacity - furni.ownedBooks.size
                    val slicedBooksToAssign = booksToAssign.take(capacity)
                    if (capacity == 0) {
                        messageRepo.addMessage(MessageType.Negative, "There are already $furnitureCapacity books here!")
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