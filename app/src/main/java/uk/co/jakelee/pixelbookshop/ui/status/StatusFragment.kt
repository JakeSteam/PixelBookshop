package uk.co.jakelee.pixelbookshop.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.fragment_status.*
import kotlinx.android.synthetic.main.fragment_status.view.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.dao.PlayerDao
import uk.co.jakelee.pixelbookshop.database.entity.Message
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
        statusViewModel.getBookData().observe(viewLifecycleOwner, stockObserver)
        statusViewModel.getCoinData().observe(viewLifecycleOwner, coinObserver)
        statusViewModel.xp.observe(viewLifecycleOwner, xpObserver)
        statusViewModel.date.observe(viewLifecycleOwner, dateObserver)
        statusViewModel.messages.observe(viewLifecycleOwner, messagesObserver)

        val root = inflater.inflate(R.layout.fragment_status, container, false)
        root.text_level_progress.setOnClickListener { xpClick() }
        root.text_stock_progress.setOnClickListener {
            if (findNavController().currentDestination?.label == "ShopFragment") {
                findNavController().navigate(R.id.action_shopFragment_to_stockFragment)
            } else {
                findNavController().popBackStack(R.id.shopFragment, false)
            }
        }
        root.text_messages_progress.setOnClickListener {
            if (findNavController().currentDestination?.label == "ShopFragment") {
                findNavController().navigate(R.id.action_shopFragment_to_messageFragment)
            } else {
                findNavController().popBackStack(R.id.shopFragment, false)
            }
        }
        return root
    }

    var currentBalloon: Balloon? = null

    private fun xpClick() {
        currentBalloon?.dismiss()
        currentBalloon = Balloon.Builder(activity!!)
            .setText(statusViewModel.xp.value?.let {
                "$it / ${Xp.levelToXp(Xp.getNextLevel(it))}xp to level ${Xp.getNextLevel(it)}"
            } ?: "")
            .setTextSize(22f)
            .setArrowPosition(0.15f)
            .setWidthRatio(0.5f)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setTextTypeface(ResourcesCompat.getFont(activity!!, R.font.fenwickwoodtype)!!)
            .setTextColorResource(R.color.black)
            .setBackgroundColorResource(R.color.colorPrimary)
            .setLifecycleOwner(viewLifecycleOwner)
            .build().also {
                it.showAlignBottom(text_level_progress)
            }
    }

    private val stockObserver: Observer<StatusViewModel.BookData> = Observer {
        it?.let {
            if (!it.isValid()) return@Observer
            text_stock.text = String.format(
                Locale.UK, getString(R.string.status_stock),
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
            val formattedTime = SimpleDateFormat("ha", Locale.ROOT).format(calendar.time).toLowerCase()
            text_time.text = String.format(
                Locale.UK,
                getString(R.string.status_day),
                it.day,
                formattedTime
            )
        }
    }

    private val messagesObserver: Observer<List<Message>> = Observer {
        it?.let { messages ->
            val unreadMessages = messages.filter { !it.dismissed }
            val size = minOf(99, unreadMessages.size)
            text_messages.text = "$size"
            text_messages_progress.progress = size
        }
    }
}