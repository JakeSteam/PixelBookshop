package uk.co.jakelee.pixelbookshop.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_status.*
import uk.co.jakelee.pixelbookshop.R

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProviders.of(this).get(StatusViewModel::class.java)
        statusViewModel.level.observe(this, Observer { text_level.text = it })
        statusViewModel.xp.observe(this, Observer { text_xp.text = it })
        statusViewModel.stock.observe(this, Observer { text_stock.text = it })
        statusViewModel.coins.observe(this, Observer { text_coins.text = it })
        return inflater.inflate(R.layout.fragment_status, container, false)
    }
}