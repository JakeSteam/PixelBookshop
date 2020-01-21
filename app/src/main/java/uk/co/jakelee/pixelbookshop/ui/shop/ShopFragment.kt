package uk.co.jakelee.pixelbookshop.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_shop.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedFloor


class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(uk.co.jakelee.pixelbookshop.R.layout.fragment_shop, container, false)
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        return root
    }

    override fun onResume() {
        super.onResume()
        floor_layer.removeAllViews()

        // Floor
        val floors = getFloor()

        // Sizes used for padding calculations
        val tileHeight = 128
        val tileWidth = 256
        val yTiles = floors.size
        val xTiles = floors.first().size

        for (floor in floors) {
            for (tile in floor) {
                val layoutParams = RelativeLayout.LayoutParams(256, 512) // Sizes used for size calculations
                val leftPadding = (tile.x + tile.y) * (tileWidth / 2)
                val topPadding = (tile.y + xTiles - tile.x) * (tileHeight / 2)
                layoutParams.setMargins(leftPadding, topPadding, 0, 0)

                val image = ImageView(activity!!).apply {
                    if (tile.exists) setImageResource(R.drawable.floor_planks)
                    setOnClickListener {
                        Toast.makeText(
                            activity,
                            "Clicked tile (${tile.x},${tile.y})!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                floor_layer.addView(image, layoutParams)
            }
        }
    }

    // List[Y][X]
    private fun getFloor(): List<List<OwnedFloor>> {
        return listOf(
            listOf(
                OwnedFloor(0, 3, 0, true),
                OwnedFloor(0, 2, 0, true),
                OwnedFloor(0, 1, 0, true),
                OwnedFloor(0, 0, 0, true)
            ), listOf(
                OwnedFloor(0, 3, 1, true),
                OwnedFloor(0, 2, 1, false),
                OwnedFloor(0, 1, 1, true),
                OwnedFloor(0, 0, 1, false)
            ), listOf(
                OwnedFloor(0, 3, 2, false),
                OwnedFloor(0, 2, 2, false),
                OwnedFloor(0, 1, 2, true),
                OwnedFloor(0, 0, 2, false)
            ), listOf(
                OwnedFloor(0, 3, 3, true),
                OwnedFloor(0, 2, 3, false),
                OwnedFloor(0, 1, 3, true),
                OwnedFloor(0, 0, 3, true)
            )
        )
    }

}