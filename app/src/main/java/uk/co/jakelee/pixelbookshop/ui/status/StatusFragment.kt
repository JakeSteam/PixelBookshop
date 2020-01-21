package uk.co.jakelee.pixelbookshop.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_status.*
import uk.co.jakelee.pixelbookshop.R

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        statusViewModel.ownedBookCount.observe(this.activity!!, Observer { it?.let {
            text_stock.text = "$it books"
        }})
        statusViewModel.xp.observe(this.activity!!, Observer { it?.let {
            text_xp.text = "$it XP"
            text_level.text = "${it.div(10)}"
        }})
        statusViewModel.coins.observe(this.activity!!, Observer { it?.let {
            text_coins.text = "$it"
        }})
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        root.findViewById<TextView>(R.id.text_xp).setOnClickListener {
            statusViewModel.addXp(10)
        }
        return root
    }
}