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
import uk.co.jakelee.pixelbookshop.maths.Xp
import kotlin.math.ln
import kotlin.math.pow

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        statusViewModel.getBookData().observe(this.activity!!, Observer { it?.let {
            if (!it.isValid()) return@Observer
            text_stock.text = "${it.books} / ${it.max}"
            text_stock_progress.max = it.max!!
            text_stock_progress.progress = it.books!!
        }})
        statusViewModel.getCoinData().observe(this.activity!!, Observer { it?.let {
            if (!it.isValid()) return@Observer
            text_coins.text = it.coins!!.withSuffix()
            text_coins_progress.max = it.max!!
            text_coins_progress.progress = it.coins!!
        }})
        statusViewModel.xp.observe(this.activity!!, Observer { it?.let {
            val level = Xp.xpToLevel(it)
            text_level.text = "$level"
            text_level_progress.progress = Xp.getLevelProgress(it)
        }})
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    private fun Int.withSuffix(): String {
        if (this < 1000) return "" + this
        val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
        return String.format(
            "%.1f%c",
            this / 1000.0.pow(exp.toDouble()),
            "kMGTPE"[exp - 1]
        )
    }
}