package uk.co.jakelee.pixelbookshop.ui.shop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.lookups.Floor
import uk.co.jakelee.pixelbookshop.lookups.Wall
import uk.co.jakelee.pixelbookshop.repository.OwnedFloorRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository
import uk.co.jakelee.pixelbookshop.repository.PlayerRepository

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository
    private val playerRepo: PlayerRepository

    val ownedFloor: LiveData<List<OwnedFloor>>
    val ownedFurniture: LiveData<List<OwnedFurniture>>
    val wall: LiveData<Wall>

    init {
        val ownedFloorDao = AppDatabase.getDatabase(application, viewModelScope).ownedFloorDao()
        ownedFloorRepo = OwnedFloorRepository(ownedFloorDao)
        ownedFloor = ownedFloorRepo.allFloor

        val ownedFurnitureDao =
            AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        ownedFurniture = ownedFurnitureRepo.allFurniture

        val playerDao = AppDatabase.getDatabase(application, viewModelScope).playerDao()
        playerRepo = PlayerRepository(playerDao)
        wall = playerRepo.wall
    }

    fun upgradeFloor(ownedFloor: OwnedFloor) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            ownedFloor.apply {
                if (!isFacingEast) {
                    isFacingEast = true
                } else {
                    isFacingEast = false
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
            }
            ownedFloorRepo.insert(ownedFloor)
        }
    }

    fun upgradeWall(wall: Wall) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            playerRepo.changeWall(
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
                }
            )
        }
    }
}