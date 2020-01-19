package uk.co.jakelee.pixelbookshop.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uk.co.jakelee.pixelbookshop.R

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shop, container, false)

        shopViewModel.text.observe(this.activity!!, Observer {
            root.findViewById<TextView>(R.id.text_shop).text = it
        })
        root.findViewById<TextView>(R.id.text_xp).setOnClickListener {
            shopViewModel.addXp(10)
        }
        return root
    }

    fun add10Xp() {
        shopViewModel.addXp(10)
    }
}