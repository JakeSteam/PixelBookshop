package uk.co.jakelee.pixelbookshop.ui.stock

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class StockAdapter(var context: Activity, private val books: List<OwnedBook>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.item_book_row, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    private val selectedItems = mutableListOf<Int>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = books[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.name.text = String.format("%1s by %2s %3s", book.book.title, book.book.authorFirstName, book.book.authorSurname)
        viewHolder.name.alpha = if (selectedItems.contains(book.id)) 0.5f else 1f
        viewHolder.name.setOnClickListener {
            if (selectedItems.contains(book.id)) {
                selectedItems.remove(book.id)
                it.alpha = 1f
            } else {
                selectedItems.add(book.id)
                it.alpha = 0.5f
            }
        }

        viewHolder.genre.text = context.getString(book.book.genre.title)
        viewHolder.rarity.text = context.getString(book.book.rarity.title)
        viewHolder.published.text = book.book.published.toString()

        viewHolder.type.text = context.getString(book.bookType.title)
        viewHolder.source.text = context.getString(book.bookSource.title)
        viewHolder.quality.text = context.getString(book.bookQuality.title)
        viewHolder.defect.text = context.getString(book.bookDefect.title)

        val furnitureResource = if (book.ownedFurnitureId ?: 0 > 0) R.drawable.ic_store_green else android.R.color.transparent
        viewHolder.furniture.setImageResource(furnitureResource)

        viewHolder.url.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(book.book.url))) 
        }
    }

    override fun getItemCount() = books.size

    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)

        var genre: TextView = itemView.findViewById(R.id.genre)
        var rarity: TextView = itemView.findViewById(R.id.rarity)
        var published: TextView = itemView.findViewById(R.id.published)

        var type: TextView = itemView.findViewById(R.id.type)
        var source: TextView = itemView.findViewById(R.id.source)
        var quality: TextView = itemView.findViewById(R.id.quality)
        var defect: TextView = itemView.findViewById(R.id.defect)
        var furniture: ImageView = itemView.findViewById(R.id.furniture)
        var url: ImageView = itemView.findViewById(R.id.url)
    }
}