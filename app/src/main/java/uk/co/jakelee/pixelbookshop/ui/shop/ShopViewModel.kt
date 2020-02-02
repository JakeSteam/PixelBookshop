package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo
import uk.co.jakelee.pixelbookshop.lookups.Floor
import uk.co.jakelee.pixelbookshop.lookups.Furniture
import uk.co.jakelee.pixelbookshop.lookups.Wall
import uk.co.jakelee.pixelbookshop.repository.OwnedFloorRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.ShopRepository
import java.util.*


class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val shopRepo: ShopRepository

    private val ownedFloor: LiveData<List<OwnedFloor>>
    private val ownedFurniture: LiveData<List<OwnedFurnitureWithOwnedBooks>>
    private val wall: LiveData<WallInfo>

    var currentTab = MutableLiveData(ShopFragment.SelectedTab.NONE)

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
    }

    // WALL
    fun wallClick(wall: WallInfo, shopId: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> upgradeWall(wall.wall, shopId)
                else -> null
            }
        }
    }

    suspend fun upgradeWall(wall: Wall, id: Int) = shopRepo.changeWall(
        when (wall) {
            Wall.StoneWindow -> Wall.Fence
            Wall.Fence -> Wall.BrickFrame
            Wall.BrickFrame -> Wall.BrickPartial
            Wall.BrickPartial -> Wall.Brick
            Wall.Brick -> Wall.BrickNoSupport
            Wall.BrickNoSupport -> Wall.BrickWindow
            Wall.BrickWindow -> Wall.StoneSmall
            Wall.StoneSmall -> Wall.Stone
            Wall.Stone -> Wall.StoneHoles
            Wall.StoneHoles -> Wall.StoneWindow
        }, id
    )

    // FLOOR
    fun floorClick(floor: OwnedFloor) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> upgradeFloor(floor)
                ShopFragment.SelectedTab.ROTATE -> rotateFloor(floor)
                else -> null
            }
        }
    }

    suspend fun upgradeFloor(ownedFloor: OwnedFloor) {
        ownedFloor.apply {
            floor = when (floor) {
                Floor.Stone -> Floor.Dirt
                Floor.Dirt -> Floor.WoodOld
                Floor.WoodOld -> Floor.Wood
                Floor.Wood -> Floor.StoneUneven
                Floor.StoneUneven -> Floor.StoneMissing
                Floor.StoneMissing -> Floor.Stone
                else -> null
            }
        }
        ownedFloorRepo.insert(ownedFloor)
    }

    suspend fun rotateFloor(ownedFloor: OwnedFloor) {
        ownedFloor.apply { isFacingEast = !isFacingEast }
        ownedFloorRepo.insert(ownedFloor)
    }

    // FURNITURE
    fun furniClick(furni: OwnedFurniture) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            when (currentTab.value) {
                ShopFragment.SelectedTab.UPGRADE -> upgradeFurni(furni)
                ShopFragment.SelectedTab.ROTATE -> rotateFurni(furni)
                ShopFragment.SelectedTab.MOVE -> {}
                else -> null
            }
        }
    }

    suspend fun upgradeFurni(furniture: OwnedFurniture) {
        furniture.apply {
            this.furniture = Furniture.values()[Random().nextInt(Furniture.values().size)]
        }
        ownedFurnitureRepo.insert(furniture)
    }

    suspend fun rotateFurni(ownedFurniture: OwnedFurniture) {
        ownedFurniture.apply { isFacingEast = !isFacingEast }
        ownedFurnitureRepo.insert(ownedFurniture)
    }
}