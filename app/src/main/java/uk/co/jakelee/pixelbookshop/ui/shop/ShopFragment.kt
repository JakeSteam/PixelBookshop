package uk.co.jakelee.pixelbookshop.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurniture
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFurnitureWithOwnedBooks
import uk.co.jakelee.pixelbookshop.database.entity.WallInfo

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    enum class SelectedTab(val wall: Boolean, val floor: Boolean, val furniture: Boolean) {
        NONE(true, true, true),
        ROTATE(true, false, true),
        MOVE(false, false, true),
        UPGRADE(true, true, true)
    }

    private val visibleAlpha = 1.0f
    private val invisibleAlpha = 0.5f
    private lateinit var tileRenderer: TileRenderer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(uk.co.jakelee.pixelbookshop.R.layout.fragment_shop, container, false)
        tileRenderer = TileRenderer(activity!!)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        root.button_show.setOnClickListener { showControls() }
        root.button_hide.setOnClickListener { hideControls() }
        root.button_rotate.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.ROTATE) }
        root.button_upgrade.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.UPGRADE) }
        root.button_move.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.MOVE) }
        shopViewModel.getShopData().observe(viewLifecycleOwner, shopDataObserver)
        shopViewModel.currentTab.observe(viewLifecycleOwner, shopTabObserver)
        return root
    }

    private val shopTabObserver = Observer<SelectedTab> {
        setButtonVisibility(button_rotate, SelectedTab.ROTATE, it)
        setButtonVisibility(button_upgrade, SelectedTab.UPGRADE, it)
        setButtonVisibility(button_move, SelectedTab.MOVE, it)
        setSelectedLayers(it)
    }

    private val shopDataObserver = Observer<ShopData> { result ->
        if (!result.isValid()) return@Observer
        val maxX = result.floors!!.last().x
        val maxY = result.floors!!.first().y
        drawFloors(result.floors!!, maxX)
        drawWalls(result.floors!!, result.wall!!, maxX, maxY)
        drawFurnitures(result.furnitures!!, maxX)
    }

    private fun setButtonVisibility(button: View, targetTab: SelectedTab, actualTab: SelectedTab) {
        val isVisible = actualTab == SelectedTab.NONE || actualTab == targetTab
        button.alpha = if (isVisible) visibleAlpha else invisibleAlpha
    }

    private fun setSelectedLayers(selectedTab: SelectedTab) {
        floor_layer.alpha = if (selectedTab.floor) visibleAlpha else invisibleAlpha
        wall_layer.alpha = if (selectedTab.wall) visibleAlpha else invisibleAlpha
        furniture_layer.alpha = if (selectedTab.furniture) visibleAlpha else invisibleAlpha
    }

    private fun showControls() {
        controls.visibility = View.VISIBLE
        button_show.visibility = View.GONE
    }

    private fun hideControls() {
        controls.visibility = View.GONE
        button_show.visibility = View.VISIBLE
        shopViewModel.setOrResetMode(SelectedTab.NONE)
    }

    private fun drawFloors(floors: List<OwnedFloor>, maxX: Int) {
        floor_layer.removeAllViews()
        floors.forEach {
            val callback = { view: View, floor: OwnedFloor ->
                shopViewModel.floorClick(floor)
            }
            tileRenderer.drawFloor(floor_layer, maxX, it, callback)
        }
    }

    private fun drawWalls(floors: List<OwnedFloor>, wallInfo: WallInfo, maxX: Int, maxY: Int) {
        wall_layer.removeAllViews()
        val callback = { view: View, wall: WallInfo ->
            shopViewModel.wallClick(wall, 1) }
        floors
            .filter { it.x == 0 || it.y == maxY }
            .forEach {
                tileRenderer.drawWall(wall_layer, it.x, it.y, maxX, maxY, wallInfo, callback)
            }
    }

    private fun drawFurnitures(furnitures: List<OwnedFurnitureWithOwnedBooks>, maxX: Int) {
        furniture_layer.removeAllViews()
        furnitures.forEach {
            val callback = { view: View, tile: OwnedFurniture ->
                shopViewModel.furniClick(tile) }
            tileRenderer.drawFurniture(furniture_layer, it, maxX, callback)
        }
    }

}