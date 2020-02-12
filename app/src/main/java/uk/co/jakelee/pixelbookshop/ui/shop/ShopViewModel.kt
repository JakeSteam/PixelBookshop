package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.repository.OwnedFloorRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository
import uk.co.jakelee.pixelbookshop.repository.ShopRepository

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val shopRepo: ShopRepository
    private val playerRepo: PlayerRepository

    private val ownedFloor: LiveData<List<OwnedFloor>>
    private val ownedFurniture: LiveData<List<OwnedFurnitureWithOwnedBooks>>
    private val wall: LiveData<WallInfo>
    private val player: LiveData<Player>

    private var selectedFurni: OwnedFurniture? = null

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
        shopRepo = ShopRepository(shopDao, 1)
        wall = shopRepo.wall

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        player = playerRepo.player
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


    fun wallClick(wall: WallInfo, shopId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> wall.wall.also {
                    if (playerRepo.canPurchase(it.cost, 0)) {
                        shopRepo.upgradeWall(it, shopId)
                        playerRepo.purchase(it.cost)
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
                    if (playerRepo.canPurchase(it.cost, 0)) {
                        ownedFloorRepo.upgradeFloor(floor)
                        playerRepo.purchase(it.cost)
                    }
                }
                ShopFragment.SelectedTab.ROTATE -> ownedFloorRepo.rotateFloor(floor)
                ShopFragment.SelectedTab.MOVE -> {
                    ownedFurnitureRepo.moveFurni(floor, selectedFurni)
                    selectedFurni = null
                }
                else -> null
            }
        }
    }

    fun furniClick(furni: OwnedFurniture) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> furni.furniture.also {
                    if (playerRepo.canPurchase(it.cost, it.level)) {
                        ownedFurnitureRepo.upgradeFurni(furni)
                        playerRepo.purchase(it.cost)
                    }
                }
                ShopFragment.SelectedTab.ROTATE -> ownedFurnitureRepo.rotateFurni(furni)
                ShopFragment.SelectedTab.MOVE -> {
                    if (selectedFurni == null) {
                        selectedFurni = furni
                    } else {
                        ownedFurnitureRepo.swap(furni, selectedFurni!!)
                        selectedFurni = null
                    }
                }
                else -> null
            }
        }
    }
}