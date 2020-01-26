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
import uk.co.jakelee.pixelbookshop.repository.OwnedFloorRepository
import uk.co.jakelee.pixelbookshop.repository.OwnedFurnitureRepository

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    private val ownedFloorRepo: OwnedFloorRepository
    private val ownedFurnitureRepo: OwnedFurnitureRepository

    val ownedFloor: LiveData<List<OwnedFloor>>
    val ownedFurniture: LiveData<List<OwnedFurniture>>

    init {
        val ownedFloorDao = AppDatabase.getDatabase(application, viewModelScope).ownedFloorDao()
        ownedFloorRepo = OwnedFloorRepository(ownedFloorDao)
        ownedFloor = ownedFloorRepo.allFloor

        val ownedFurnitureDao = AppDatabase.getDatabase(application, viewModelScope).ownedFurnitureDao()
        ownedFurnitureRepo = OwnedFurnitureRepository(ownedFurnitureDao)
        ownedFurniture = ownedFurnitureRepo.allFurniture
    }

    fun invertFloor(ownedFloor: OwnedFloor) = viewModelScope.launch {
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
                        Floor.Wood-> Floor.StoneUneven
                        Floor.StoneUneven-> Floor.StoneMissing
                        Floor.StoneMissing-> Floor.Stone
                        else -> null
                    }
                }
            }
            ownedFloorRepo.insert(ownedFloor)
        }
    }
}