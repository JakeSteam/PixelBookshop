package uk.co.jakelee.pixelbookshop.ui.stock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import uk.co.jakelee.pixelbookshop.database.AppDatabase
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook
import uk.co.jakelee.pixelbookshop.repository.OwnedBookRepository

class StockViewModel(application: Application) : AndroidViewModel(application) {

    private val booksRepo: OwnedBookRepository

    val books: LiveData<List<OwnedBook>>

    init {
        val booksDao = AppDatabase.getDatabase(application, viewModelScope).ownedBookDao()
        booksRepo = OwnedBookRepository(booksDao)
        books = booksRepo.allBooks
    }
}