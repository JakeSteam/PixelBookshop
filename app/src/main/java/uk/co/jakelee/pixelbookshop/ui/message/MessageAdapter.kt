package uk.co.jakelee.pixelbookshop.ui.message

import android.app.Activity
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.Message

class MessageAdapter(var context: Activity, private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.item_message_row, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.time.text = DateUtils.getRelativeTimeSpanString(message.time)
        viewHolder.message.text = message.message
    }

    override fun getItemCount() = messages.size

    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.findViewById(R.id.time)
        var message: TextView = itemView.findViewById(R.id.message)
    }
}