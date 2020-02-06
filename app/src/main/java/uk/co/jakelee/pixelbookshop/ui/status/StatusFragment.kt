package uk.co.jakelee.pixelbookshop.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_status.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.converters.Xp

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        statusViewModel.storageInfo.observe(this.activity!!, Observer { it?.let {
            text_stock.text = "${it.first} / ${it.second}"
        }})
        statusViewModel.xp.observe(this.activity!!, Observer { it?.let {
            val level = Xp.xpToLevel(it)
            text_level.text = "$level"
            text_level_progress.progress = Xp.getLevelProgress(it)
        }})
        statusViewModel.coins.observe(this.activity!!, Observer { it?.let {
            text_coins.text = "$it"
        }})
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        /*root.findViewById<TextView>(R.id.text_xp).setOnClickListener {
            statusViewModel.addXp(10)
        }*/
        return root
    }
}