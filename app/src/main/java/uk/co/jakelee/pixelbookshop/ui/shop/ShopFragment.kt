package uk.co.jakelee.pixelbookshop.ui.shop

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.*
import uk.co.jakelee.pixelbookshop.extensions.setAllEnabled
import uk.co.jakelee.pixelbookshop.lookups.MessageType
import uk.co.jakelee.pixelbookshop.utils.GameTimeHelper

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    enum class SelectedTab(val wall: Boolean, val floor: Boolean, val furniture: Boolean) {
        NONE(true, true, true),
        ROTATE(false, true, true),
        MOVE(true, false, true),
        UPGRADE(true, true, true),
        ASSIGN(false, false, true)
    }

    private val visibleAlpha = 1.0f
    private val invisibleAlpha = 0.5f
    private lateinit var tileRenderer: TileRenderer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shop, container, false)
        tileRenderer = TileRenderer(activity!!)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        root.button_show.setOnClickListener { showControls() }
        root.button_hide.setOnClickListener { hideControls() }
        root.button_rotate.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.ROTATE) }
        root.button_upgrade.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.UPGRADE) }
        root.button_move.setOnClickListener { shopViewModel.setOrResetMode(SelectedTab.MOVE) }
        root.button_arrow_sw.setOnClickListener { shopViewModel.addStrip(false, false) }
        root.button_arrow_ne.setOnClickListener { shopViewModel.addStrip(true, false) }
        root.button_arrow_nw.setOnClickListener { shopViewModel.addStrip(false, true) }
        root.button_arrow_se.setOnClickListener { shopViewModel.addStrip(true, true) }
        root.button_travel.setOnClickListener { shopViewModel.toggleShop() }
        root.alert.setOnClickListener {
            alert.visibility = View.GONE
            shopViewModel.markMessageAsDismissed()
        }
        shopViewModel.getShopData().observe(viewLifecycleOwner, shopDataObserver)
        shopViewModel.getTickData().observe(viewLifecycleOwner, tickObserver)
        shopViewModel.currentTab.observe(viewLifecycleOwner, shopTabObserver)
        shopViewModel.latestMessage.observe(viewLifecycleOwner, latestMessageObserver)
        shopViewModel.pendingPurchases.observe(viewLifecycleOwner, Observer { })
        shopViewModel.ownedFurniture.observe(viewLifecycleOwner, Observer { })
        shopViewModel.coins.observe(viewLifecycleOwner, Observer { })
        shopViewModel.xp.observe(viewLifecycleOwner, Observer { })
        shopViewModel.selectedFurni.observe(viewLifecycleOwner, selectedFurniObserver)

        handleArguments()
        return root
    }

    private fun handleArguments() {
        arguments?.let {
            val booksToAssign = ShopFragmentArgs.fromBundle(it).booksToAssign
            booksToAssign?.let {
                if (booksToAssign.isEmpty()) { return }
                shopViewModel.booksToAssign = booksToAssign.toTypedArray()
                shopViewModel.setOrResetMode(SelectedTab.ASSIGN)
                arguments = null
            }
        }
    }

    private var visitorLocation: ImageView? = null
    private val tickObserver = Observer<ShopUiUpdate> { result ->
        val isDuringDay = GameTimeHelper.isDuringDay(result.time.hour)
        customiseControls.alpha = if (isDuringDay) 0.5f else 1.0f
        customiseControls.setAllEnabled(!isDuringDay)
        travelControls.alpha = if (isDuringDay) 0.5f else 1.0f
        travelControls.setAllEnabled(!isDuringDay)

        result.visitorPosition.firstOrNull()?.let {
            Log.i("VisitorPosition", result.visitorPosition.toString())
            if (it.second != null) {
                visitorLocation?.alpha = 1f
                visitorLocation = view!!.findViewById(it.second!!.id)
                visitorLocation!!.alpha = 0.5f
            }
        }
    }

    private val latestMessageObserver = Observer<Message> {
        it?.let {
            if (it.dismissed) {
                alert.visibility = View.GONE
            } else {
                alertText.text = it.message
                val resource = if (it.type == MessageType.Positive) R.drawable.ui_element else R.drawable.ui_element_greyscale
                alertText.setBackgroundResource(resource)
                alert.visibility = View.VISIBLE
            }
        }
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

    private var lastSelectedFurniId: Int? = null
    private val selectedFurniObserver = Observer<OwnedFurniture?> { id ->
        lastSelectedFurniId?.let {
            ImageViewCompat.setImageTintList(view!!.findViewById(it), null)
        }
        id?.let {
            val view = view!!.findViewById<ImageView>(it.id)
            val colour = ColorStateList.valueOf(ContextCompat.getColor(activity!!, R.color.furniture_move_highlight))
            ImageViewCompat.setImageTintMode(view, PorterDuff.Mode.MULTIPLY)
            ImageViewCompat.setImageTintList(view, colour)
        }
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
        floors
            .filter { it.x == 0 || it.y == maxY }
            .forEach {
                val callback = { view: View, wall: WallInfo ->
                    shopViewModel.wallClick(wall, it.x, it.y, 1)
                }
                tileRenderer.drawWall(wall_layer, it.x, it.y, maxX, maxY, wallInfo, callback)
            }
    }

    private fun drawFurnitures(furnitures: List<OwnedFurnitureWithOwnedBooks>, maxX: Int) {
        furniture_layer.removeAllViews()
        furnitures.forEach {
            val callback = { view: View, tile: OwnedFurniture ->
                shopViewModel.furniClick(it)
            }
            tileRenderer.drawFurniture(furniture_layer, it, maxX, callback)
        }
    }

}