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
import uk.co.jakelee.pixelbookshop.database.entity.Message
import uk.co.jakelee.pixelbookshop.dto.GameTime
import uk.co.jakelee.pixelbookshop.utils.FormatHelper
import uk.co.jakelee.pixelbookshop.utils.GameTimeHelper
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
        statusViewModel.isPlaying.observe(viewLifecycleOwner, isPlayingObserver)
        statusViewModel.ownedFurniture.observe(viewLifecycleOwner, Observer { })

        val root = inflater.inflate(R.layout.fragment_status, container, false)
        root.text_level_progress.setOnClickListener { xpClick() }
        root.time_control.setOnClickListener { statusViewModel.toggleTime() }
        root.text_stock_progress.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.stockFragment) {
                findNavController().popBackStack(R.id.shopFragment, false)
            } else {
                findNavController().navigate(R.id.action_global_stockFragment)
            }
        }
        root.text_messages_progress.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.messageFragment) {
                findNavController().popBackStack(R.id.shopFragment, false)
            } else {
                findNavController().navigate(R.id.action_global_messageFragment)
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

    private val isPlayingObserver: Observer<Boolean> = Observer {
        time_control.setImageResource(if (it) R.drawable.ic_pause else R.drawable.ic_play)
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
            text_level.text = Xp.xpToLevel(it).toString()
            text_level_progress.progress = Xp.getLevelProgress(it)
        }
    }

    private val dateObserver: Observer<GameTime> = Observer {
        // Hour assumes 8am = 0. As such, 8 hours are added before displaying to user.
        it?.let {
            val isDuringDay = GameTimeHelper.isDuringDay(it.hour)
            text_stock_progress.alpha = if (isDuringDay) 0.5f else 1.0f
            text_stock_progress.isClickable = !isDuringDay

            text_time_progress.progress = it.hour
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, GameTimeHelper.internalHourToUiHour(it.hour))
            }
            val formattedTime = SimpleDateFormat(getString(R.string.format_hours), Locale.ROOT)
                .format(calendar.time)
                .toLowerCase(Locale.getDefault())
            text_time.text = String.format(Locale.UK, getString(R.string.status_day), it.day, formattedTime)
        }
    }

    private val messagesObserver: Observer<List<Message>> = Observer { messageList ->
        messageList?.let { messages ->
            val unreadMessages = messages.filter { !it.dismissed }
            val size = minOf(99, unreadMessages.size)
            text_messages.text = "$size"
            text_messages_progress.progress = size
        }
    }
}