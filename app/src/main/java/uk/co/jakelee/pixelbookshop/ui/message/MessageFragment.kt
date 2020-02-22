package uk.co.jakelee.pixelbookshop.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_message.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.Message

class MessageFragment : Fragment() {

    private lateinit var messageViewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_message, container, false)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        messageViewModel.messages.observe(viewLifecycleOwner, messagesObserver)
        return root
    }

    private val messagesObserver = Observer<List<Message>> { messages ->
        if (messages != null) {
            messagesRecycler.layoutManager = LinearLayoutManager(activity)
            messagesRecycler.adapter = MessageAdapter(activity!!, messages)
        }
    }
}