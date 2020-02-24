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
import uk.co.jakelee.pixelbookshop.lookups.*

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
        root.filtering.setOnClickListener { filterDialog.show() }
        return root
    }

    enum class SortFields(val resource: Int) {
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

    private val filterFields =
        SortFields.values().filter { it != SortFields.AUTHOR && it != SortFields.TITLE }

    private val publishedDate = listOf(2020, 2010, 2000, 1990, 1980, 1970, 1960, 1950, 1940, 1930, 1920, 1910, 1900, 1800, 1700, 1600, 1500)

    enum class SortOrder(val resource: Int) {
        ASCENDING(R.string.book_sort_ascending),
        DESCENDING(R.string.book_sort_descending)
    }

    private val sortDialog by lazy {
        TwoStepPickerDialog.Builder(activity!!)
            .withBaseData(SortFields.values().map { getString(it.resource) }.toMutableList())
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

    private val filterDialog by lazy {
        val furnitureId = listOf(false, true)
        TwoStepPickerDialog.Builder(activity!!)
            .withBaseData(filterFields.map { getString(it.resource) }.toMutableList())
            .withOnStepDataRequested { step ->
                when (filterFields[step]) {
                    SortFields.TITLE, SortFields.AUTHOR -> listOf()
                    SortFields.GENRE -> BookGenre.values().map { getString(it.title) }
                    SortFields.RARITY -> BookRarity.values().map { getString(it.title) }
                    SortFields.BOOKTYPE -> OwnedBookType.values().map { getString(it.title) }
                    SortFields.BOOKSOURCE -> OwnedBookSource.values().map { getString(it.title) }
                    SortFields.BOOKQUALITY -> OwnedBookQuality.values().map { getString(it.title) }
                    SortFields.BOOKDEFECT -> OwnedBookDefect.values().map { getString(it.title) }
                    SortFields.PUBLISHED -> publishedDate.map {
                        String.format(getString(R.string.stock_published_filter), it)
                    }
                    SortFields.FURNITUREID -> furnitureId.map { getString(if (it) R.string.stock_in_furniture else R.string.stock_in_storage) }
                }.toMutableList()
            }
            .withOkButton(getString(R.string.stock_dialog_order))
            .withCancelButton(getString(R.string.stock_dialog_cancel))
            .withDialogListener(object : OnStepPickListener {
                override fun onStepPicked(step: Int, position: Int) {
                    stockViewModel.filterBooks(step, position)
                }

                override fun onDismissed() {
                    stockViewModel.filterBooks(-1, -1)
                }
            })
            .build()
    }

    private val stockObserver = Observer<FullBookData> { data ->
        if (data.isValid()) {
            var books = data.books!!

            if (data.filterField!!.first >= 0) {
                val filterField = filterFields[data.filterField!!.first]
                val filterFieldIndex = data.filterField!!.second
                books = when (filterField) {
                    SortFields.TITLE, SortFields.AUTHOR -> books
                    SortFields.GENRE -> books.filter { it.book.genre == BookGenre.values()[filterFieldIndex] }
                    SortFields.RARITY -> books.filter { it.book.rarity == BookRarity.values()[filterFieldIndex] }
                    SortFields.PUBLISHED -> books.filter { it.book.published < publishedDate[filterFieldIndex] }
                    SortFields.BOOKTYPE -> books.filter { it.bookType == OwnedBookType.values()[filterFieldIndex] }
                    SortFields.BOOKSOURCE -> books.filter { it.bookSource == OwnedBookSource.values()[filterFieldIndex] }
                    SortFields.BOOKQUALITY -> books.filter { it.bookQuality == OwnedBookQuality.values()[filterFieldIndex] }
                    SortFields.BOOKDEFECT -> books.filter { it.bookDefect == OwnedBookDefect.values()[filterFieldIndex] }
                    SortFields.FURNITUREID -> books.filter {
                        if (filterFieldIndex > 0) {
                            it.ownedFurnitureId ?: 0 > 0
                        } else {
                            it.ownedFurnitureId == null
                        }
                    }
                }
                filtering.text = String.format(getString(R.string.stock_filter_text_set), getString(filterField.resource))
            } else {
                filtering.text = getString(R.string.stock_filter_text_unset)
            }

            val sortField = SortFields.values()[data.sortField!!.first]
            val sortOrder = SortOrder.values()[data.sortField!!.second]
            val isAscending = sortOrder == SortOrder.ASCENDING
            books = when (sortField) {
                SortFields.TITLE -> if (isAscending) books.sortedBy { it.book.title } else books.sortedByDescending { it.book.title }
                SortFields.AUTHOR -> if (isAscending) books.sortedBy { it.book.authorSurname } else books.sortedByDescending { it.book.authorSurname }
                SortFields.GENRE -> if (isAscending) books.sortedBy { it.book.genre } else books.sortedByDescending { it.book.genre }
                SortFields.RARITY -> if (isAscending) books.sortedBy { it.book.rarity } else books.sortedByDescending { it.book.rarity }
                SortFields.PUBLISHED -> if (isAscending) books.sortedBy { it.book.published } else books.sortedByDescending { it.book.published }
                SortFields.BOOKTYPE -> if (isAscending) books.sortedBy { it.bookType } else books.sortedByDescending { it.bookType }
                SortFields.BOOKSOURCE -> if (isAscending) books.sortedBy { it.bookSource } else books.sortedByDescending { it.bookSource }
                SortFields.BOOKQUALITY -> if (isAscending) books.sortedBy { it.bookQuality } else books.sortedByDescending { it.bookQuality }
                SortFields.BOOKDEFECT -> if (isAscending) books.sortedBy { it.bookDefect } else books.sortedByDescending { it.bookDefect }
                SortFields.FURNITUREID -> if (isAscending) books.sortedBy { it.ownedFurnitureId } else books.sortedByDescending { it.ownedFurnitureId }
            }
            sorting.text = String.format(getString(R.string.stock_sort_text), getString(sortField.resource), getString(sortOrder.resource))

            booksRecycler.adapter = StockAdapter(activity!!, books)
        }
    }
}