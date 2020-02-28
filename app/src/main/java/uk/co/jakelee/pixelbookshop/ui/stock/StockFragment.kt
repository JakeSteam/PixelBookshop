package uk.co.jakelee.pixelbookshop.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hamsa.twosteppickerdialog.OnStepPickListener
import com.hamsa.twosteppickerdialog.TwoStepPickerDialog
import kotlinx.android.synthetic.main.fragment_stock.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.lookups.*

class StockFragment : Fragment() {

    private lateinit var stockViewModel: StockViewModel

    private val filterFields = StockSortField.values().filter {
        it != StockSortField.AUTHOR && it != StockSortField.TITLE
    }

    private val publishedDate = listOf(2020, 2010, 2000, 1990, 1980, 1970, 1960, 1950, 1940, 1930, 1920, 1910, 1900, 1800, 1700, 1600, 1500)

    private val baseSortData by lazy {
        StockSortField.values().map { getString(it.resource) }.toMutableList()
    }

    private val stepSortData by lazy {
        StockSortOrder.values().map { getString(it.resource) }.toMutableList()
    }

    private var adapter: StockAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_stock, container, false)
        stockViewModel = ViewModelProvider(this).get(StockViewModel::class.java)
        stockViewModel.getBooks().observe(viewLifecycleOwner, stockObserver)
        root.selectAll.setOnClickListener {
            adapter?.let { adapter ->
                stockViewModel.selectedBooks.addAll(adapter.books.map { it.id })
                selectAll.alpha = 0.5f
                adapter.notifyDataSetChanged()
                updateAssignButton()
            }
        }
        root.assign.setOnClickListener {
            val booksToAssign = stockViewModel.selectedBooks.toIntArray()
            val action = StockFragmentDirections.actionStockFragmentToShopFragment(booksToAssign)
            findNavController().navigate(action)
        }
        root.sorting.setOnClickListener { showSortDialog() }
        root.filtering.setOnClickListener { showFilterDialog() }
        return root
    }

    private fun showSortDialog() {
        TwoStepPickerDialog
            .Builder(activity!!)
            .withInitialBaseSelected(stockViewModel.sortField.value?.first ?: 0)
            .withInitialStepSelected(stockViewModel.sortField.value?.second ?: 0)
            .withBaseData(baseSortData)
            .withOnStepDataRequested { stepSortData }
            .withOkButton(getString(R.string.stock_dialog_order))
            .withCancelButton(getString(R.string.stock_dialog_cancel))
            .withDialogListener(object : OnStepPickListener {
                override fun onStepPicked(step: Int, position: Int) {
                    stockViewModel.sortBooks(step, position)
                }

                override fun onDismissed() {}
            })
            .build()
            .show()
    }

    private fun showFilterDialog() {
        TwoStepPickerDialog.Builder(activity!!)
            .withBaseData(filterFields.map { getString(it.resource) }.toMutableList())
            .withOnStepDataRequested { getFilterSteps(it) }
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
            .show()
    }

    private val stockObserver = Observer<FullBookData> { data ->
        if (data.isValid()) {
            var books = data.books!!
            val sortField = StockSortField.values()[data.sortField!!.first]
            val sortOrder = StockSortOrder.values()[data.sortField!!.second]
            if (data.filterField!!.first >= 0) {
                val filterField = filterFields[data.filterField!!.first]
                val filterFieldIndex = data.filterField!!.second
                books = filterBooks(books, filterField, filterFieldIndex)
                filtering.text = getString(filterField.resource)
            } else {
                filtering.text = getString(R.string.stock_filter_text_unset)
            }
            books = sortBooks(books, sortField, sortOrder)
            sorting.text = String.format(
                getString(R.string.stock_sort_text),
                getString(sortField.resource),
                getString(sortOrder.resource)
            )
            updateAssignButton()
            adapter = StockAdapter(activity!!, books,
                { id: Int ->
                    stockViewModel.selectedBooks.add(id)
                    updateAssignButton()
                },
                { id: Int ->
                    stockViewModel.selectedBooks.remove(id)
                    updateAssignButton()
                },
                { id: Int -> stockViewModel.selectedBooks.contains(id) })
            booksRecycler.adapter = adapter
            selectAll.alpha = 1f
        }
    }

    private fun updateAssignButton() {
        val selectedItems = stockViewModel.selectedBooks.size
        assign.text = String.format(getString(R.string.stock_assign_button), selectedItems)
        assign.alpha = if(selectedItems > 0) 1f else 0.5f

    }

    private fun getFilterSteps(step: Int): MutableList<String> {
        val furnitureId = listOf(false, true)
        return when (filterFields[step]) {
            StockSortField.TITLE, StockSortField.AUTHOR -> listOf()
            StockSortField.GENRE -> BookGenre.values().map { getString(it.title) }
            StockSortField.RARITY -> BookRarity.values().map { getString(it.title) }
            StockSortField.BOOKTYPE -> OwnedBookType.values().map { getString(it.title) }
            StockSortField.BOOKSOURCE -> OwnedBookSource.values().map { getString(it.title) }
            StockSortField.BOOKQUALITY -> OwnedBookQuality.values().map { getString(it.title) }
            StockSortField.BOOKDEFECT -> OwnedBookDefect.values().map { getString(it.title) }
            StockSortField.PUBLISHED -> publishedDate.map {
                String.format(getString(R.string.stock_published_filter), it)
            }
            StockSortField.FURNITUREID -> furnitureId.map { getString(if (it) R.string.stock_in_furniture else R.string.stock_in_storage) }
        }.toMutableList()
    }

    private fun sortBooks(
        books: List<OwnedBook>,
        sortField: StockSortField,
        sortOrder: StockSortOrder
    ): List<OwnedBook> {
        val isAscending = sortOrder == StockSortOrder.ASCENDING
        return when (sortField) {
            StockSortField.TITLE -> if (isAscending) books.sortedBy { it.book.title } else books.sortedByDescending { it.book.title }
            StockSortField.AUTHOR -> if (isAscending) books.sortedBy { it.book.authorSurname } else books.sortedByDescending { it.book.authorSurname }
            StockSortField.GENRE -> if (isAscending) books.sortedBy { it.book.genre } else books.sortedByDescending { it.book.genre }
            StockSortField.RARITY -> if (isAscending) books.sortedBy { it.book.rarity } else books.sortedByDescending { it.book.rarity }
            StockSortField.PUBLISHED -> if (isAscending) books.sortedBy { it.book.published } else books.sortedByDescending { it.book.published }
            StockSortField.BOOKTYPE -> if (isAscending) books.sortedBy { it.bookType } else books.sortedByDescending { it.bookType }
            StockSortField.BOOKSOURCE -> if (isAscending) books.sortedBy { it.bookSource } else books.sortedByDescending { it.bookSource }
            StockSortField.BOOKQUALITY -> if (isAscending) books.sortedBy { it.bookQuality } else books.sortedByDescending { it.bookQuality }
            StockSortField.BOOKDEFECT -> if (isAscending) books.sortedBy { it.bookDefect } else books.sortedByDescending { it.bookDefect }
            StockSortField.FURNITUREID -> if (isAscending) books.sortedBy { it.ownedFurnitureId } else books.sortedByDescending { it.ownedFurnitureId }
        }
    }

    private fun filterBooks(
        books: List<OwnedBook>,
        filterField: StockSortField,
        filterFieldIndex: Int
    ): List<OwnedBook> = when (filterField) {
        StockSortField.TITLE, StockSortField.AUTHOR -> books
        StockSortField.GENRE -> books.filter { it.book.genre == BookGenre.values()[filterFieldIndex] }
        StockSortField.RARITY -> books.filter { it.book.rarity == BookRarity.values()[filterFieldIndex] }
        StockSortField.PUBLISHED -> books.filter { it.book.published < publishedDate[filterFieldIndex] }
        StockSortField.BOOKTYPE -> books.filter { it.bookType == OwnedBookType.values()[filterFieldIndex] }
        StockSortField.BOOKSOURCE -> books.filter { it.bookSource == OwnedBookSource.values()[filterFieldIndex] }
        StockSortField.BOOKQUALITY -> books.filter { it.bookQuality == OwnedBookQuality.values()[filterFieldIndex] }
        StockSortField.BOOKDEFECT -> books.filter { it.bookDefect == OwnedBookDefect.values()[filterFieldIndex] }
        StockSortField.FURNITUREID -> books.filter {
            if (filterFieldIndex > 0) {
                it.ownedFurnitureId ?: 0 > 0
            } else {
                it.ownedFurnitureId == null
            }
        }
    }
}