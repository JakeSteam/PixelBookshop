package uk.co.jakelee.pixelbookshop.ui.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.Book
import uk.co.jakelee.pixelbookshop.repository.BookRepository

class StatusViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: BookRepository

    // LiveData gives us updated words when they change.
    val allBooks: LiveData<List<Book>>

    init {
        // Gets reference to BookDao from AppDatabase to construct
        // the correct BookRepository.
        val wordsDao = AppDatabase.getDatabase(application, viewModelScope).bookDao()
        repository = BookRepository(wordsDao)
        allBooks = repository.allBooks
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(book: Book) = viewModelScope.launch {
        repository.insert(book)
    }
}