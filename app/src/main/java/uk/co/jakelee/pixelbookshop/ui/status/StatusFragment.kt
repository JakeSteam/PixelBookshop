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
import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.utils.FormatHelper
import uk.co.jakelee.pixelbookshop.utils.Xp
import java.text.SimpleDateFormat
import java.util.*

class StatusFragment : Fragment() {

    private lateinit var statusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        statusViewModel.getBookData().observe(this.activity!!, stockObserver)
        statusViewModel.getCoinData().observe(this.activity!!, coinObserver)
        statusViewModel.xp.observe(this.activity!!, xpObserver)
        statusViewModel.date.observe(this.activity!!, dateObserver)
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    private val stockObserver: Observer<StatusViewModel.BookData> = Observer {
        it?.let {
            if (!it.isValid()) return@Observer
            text_stock.text = String.format(
                Locale.UK, getString(R.string.shop_stock),
                it.assignedBooks, it.unassignedBooks, it.maxUnassignedBooks
            )
            text_stock_progress.max = it.maxUnassignedBooks!!
            text_stock_progress.progress = it.unassignedBooks!!
        }
    }

    private val coinObserver: Observer<StatusViewModel.CoinData> = Observer {
        it?.let {
            if (!it.isValid()) return@Observer
            text_coins.text = FormatHelper.int(it.coins!!)
            text_coins_progress.max = it.max!!
            text_coins_progress.progress = it.coins!!
        }
    }

    private val xpObserver: Observer<Long> = Observer {
        it?.let {
            val level = Xp.xpToLevel(it)
            text_level.text = "$level"
            text_level_progress.progress = Xp.getLevelProgress(it)
        }
    }

    private val dateObserver: Observer<PlayerDao.PlayerDate> = Observer {
        it?.let {
            text_time_progress.progress = it.hour
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, it.hour)
            }
            val formattedTime = SimpleDateFormat("ha", Locale.ROOT).format(calendar.time)
            text_time.text = String.format(
                Locale.UK,
                getString(R.string.date_progression),
                it.day,
                formattedTime
            )
        }
    }
}