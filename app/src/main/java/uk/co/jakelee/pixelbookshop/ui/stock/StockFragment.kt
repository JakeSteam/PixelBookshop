package uk.co.jakelee.pixelbookshop.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_stock.*
import uk.co.jakelee.pixelbookshop.R
import uk.co.jakelee.pixelbookshop.database.entity.OwnedBook

class StockFragment : Fragment() {

    private lateinit var stockViewModel: StockViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_stock, container, false)
        stockViewModel = ViewModelProvider(this).get(StockViewModel::class.java)
        stockViewModel.books.observe(viewLifecycleOwner, stockObserver)
        return root
    }

    private val stockObserver = Observer<List<OwnedBook>> { books ->
        if (books != null) {
            booksRecycler.layoutManager = LinearLayoutManager(activity)
            booksRecycler.adapter = StockAdapter(activity!!, books)
        }
    }
}