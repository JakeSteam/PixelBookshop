package uk.co.jakelee.pixelbookshop.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hamsa.twosteppickerdialog.OnStepPickListener
import com.hamsa.twosteppickerdialog.TwoStepPickerDialog
import kotlinx.android.synthetic.main.fragment_stock.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import uk.co.jakelee.pixelbookshop.R

class StockFragment : Fragment() {

    private lateinit var stockViewModel: StockViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_stock, container, false)
        stockViewModel = ViewModelProvider(this).get(StockViewModel::class.java)
        stockViewModel.getBooks().observe(viewLifecycleOwner, stockObserver)
        root.sorting.setOnClickListener { sortDialog.show() }
        return root
    }

    enum class SortField(val resource: Int) {
        TITLE(R.string.book_title),
        AUTHOR(R.string.book_author),
        GENRE(R.string.book_genre),
        RARITY(R.string.book_rarity),
        PUBLISHED(R.string.book_published),
        BOOKTYPE(R.string.book_type),
        BOOKSOURCE(R.string.book_source),
        BOOKQUALITY(R.string.book_quality),
        BOOKDEFECT(R.string.book_defect),
        FURNITUREID(R.string.book_furniture),
    }

    enum class SortOrder(val resource: Int) {
        ASCENDING(R.string.book_sort_ascending),
        DESCENDING(R.string.book_sort_descending)
    }

    private val sortDialog by lazy {
        TwoStepPickerDialog.Builder(activity!!)
            .withBaseData(
                SortField.values().map { getString(it.resource) }.toMutableList()
            )
            .withOnStepDataRequested {
                SortOrder.values().map { getString(it.resource) }.toMutableList()
            }
            .withOkButton(getString(R.string.stock_dialog_order))
            .withCancelButton(getString(R.string.stock_dialog_cancel))
            .withDialogListener(object : OnStepPickListener {
                override fun onStepPicked(step: Int, position: Int) {
                    stockViewModel.sortBooks(step, position)
                }

                override fun onDismissed() {}
            })
            .build()
    }

    private val stockObserver = Observer<FullBookData> { data ->
        if (data.isValid()) {
            val books = data.books!!
            val sortOrder =SortOrder.values()[data.sortOrderIndex!!]
            val sortField = SortField.values()[data.sortFieldIndex!!]

            val isAscending = sortOrder == SortOrder.ASCENDING
            val sorted = when (sortField) {
                SortField.TITLE -> if (isAscending) books.sortedBy { it.book.title } else books.sortedByDescending { it.book.title }
                SortField.AUTHOR -> if (isAscending) books.sortedBy { it.book.authorSurname } else books.sortedByDescending { it.book.authorSurname }
                SortField.GENRE -> if (isAscending) books.sortedBy { it.book.genre } else books.sortedByDescending { it.book.genre }
                SortField.RARITY -> if (isAscending) books.sortedBy { it.book.rarity } else books.sortedByDescending { it.book.rarity }
                SortField.PUBLISHED -> if (isAscending) books.sortedBy { it.book.published } else books.sortedByDescending { it.book.published }
                SortField.BOOKTYPE -> if (isAscending) books.sortedBy { it.bookType } else books.sortedByDescending { it.bookType }
                SortField.BOOKSOURCE -> if (isAscending) books.sortedBy { it.bookSource } else books.sortedByDescending { it.bookSource }
                SortField.BOOKQUALITY -> if (isAscending) books.sortedBy { it.bookQuality } else books.sortedByDescending { it.bookQuality }
                SortField.BOOKDEFECT -> if (isAscending) books.sortedBy { it.bookDefect } else books.sortedByDescending { it.bookDefect }
                SortField.FURNITUREID -> if (isAscending) books.sortedBy { it.ownedFurnitureId } else books.sortedByDescending { it.ownedFurnitureId }
            }
            sorting.text = String.format(getString(R.string.stock_sort_text), getString(sortField.resource), getString(sortOrder.resource))
            booksRecycler.adapter = StockAdapter(activity!!, sorted)
        }
    }
}