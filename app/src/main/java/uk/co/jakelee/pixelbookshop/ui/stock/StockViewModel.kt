package uk.co.jakelee.pixelbookshop.ui.stock

import android.app.Application
import androidx.lifecycle.*
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository

class StockViewModel(application: Application) : AndroidViewModel(application) {

    private val booksRepo: OwnedBookRepository

    private val books: LiveData<List<OwnedBook>>
    private val sortFieldIndex = MutableLiveData(0)
    private val sortOrderIndex = MutableLiveData(0)

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
        mediatorLiveData.addSource(sortFieldIndex) { index ->
            current.sortFieldIndex = index
            mediatorLiveData.setValue(current)
        }
        mediatorLiveData.addSource(sortOrderIndex) { index ->
            current.sortOrderIndex = index
            mediatorLiveData.setValue(current)
        }
        return mediatorLiveData
    }

    fun sortBooks(field: Int, order: Int) {
        sortFieldIndex.value = field
        sortOrderIndex.value = order
    }
}