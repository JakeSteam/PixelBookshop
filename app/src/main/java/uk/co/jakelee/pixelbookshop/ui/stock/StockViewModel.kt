package uk.co.jakelee.pixelbookshop.ui.stock

import android.app.Application
import androidx.lifecycle.*
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository

class StockViewModel(application: Application) : AndroidViewModel(application) {

    private val booksRepo: OwnedBookRepository

    private val books: LiveData<List<OwnedBook>>
    val sortField = MutableLiveData(Pair(0, 0))
    private val filterField = MutableLiveData(Pair(-1, -1))

    init {
        val booksDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        booksRepo = OwnedBookRepository(booksDao)
        books = booksRepo.allBooks
    }

    fun getBooks(): MediatorLiveData<FullBookData> {
        val mediatorLiveData = MediatorLiveData<FullBookData>()
        val current = FullBookData()
        mediatorLiveData.addSource(books) { list ->
            current.books = list
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(sortField) { field ->
            current.sortField = field
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(filterField) { field ->
            current.filterField = field
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }

    fun sortBooks(field: Int, order: Int) {
        sortField.value = Pair(field, order)
    }

    fun filterBooks(field: Int, value: Int) {
        filterField.value = Pair(field, value)
    }
}