package uk.co.jakelee.pixelbookshop.ui.stock

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_book_row.view.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class StockAdapter(
    var context: Activity,
    val books: List<OwnedBook>,
    private val addSelected: (Int) -> Unit,
    private val removeSelected: (Int) -> Unit,
    private val selectedContains: (Int) -> Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val rootView: View = LayoutInflater.from(context).inflate(R.layout.item_book_row, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = books[position]
        val view  = (holder as RecyclerViewViewHolder).view

        val isSelected = selectedContains.invoke(book.id)
        view.alpha = if (isSelected) 0.5f else 1f
        view.setOnClickListener {
            if (selectedContains.invoke(book.id)) {
                removeSelected.invoke(book.id)
                it.alpha = 1f
                it.checkbox.isChecked = false
            } else {
                addSelected.invoke(book.id)
                it.alpha = 0.5f
                it.checkbox.isChecked = true
            }
        }
        view.checkbox.isChecked = isSelected
        view.name.text = String.format(context.getString(R.string.stock_item_title), book.book.title, book.book.published, book.book.authorFirstName, book.book.authorSurname)

        view.genre.text = context.getString(book.book.genre.title)
        view.rarity.text = context.getString(book.book.rarity.title)

        view.type.text = context.getString(book.bookType.title)
        view.source.text = context.getString(book.bookSource.title)
        view.quality.text = context.getString(book.bookQuality.title)
        view.defect.text = context.getString(book.bookDefect.title)

        val furnitureResource = if (book.ownedFurnitureId ?: 0 > 0) R.drawable.ic_store_green else android.R.color.transparent
        view.furniture.setImageResource(furnitureResource)

        view.url.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(book.book.url))) 
        }
    }

    override fun getItemCount() = books.size

    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var view: ConstraintLayout = itemView as ConstraintLayout
    }
}