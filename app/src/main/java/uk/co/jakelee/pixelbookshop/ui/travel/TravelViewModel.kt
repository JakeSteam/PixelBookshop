package uk.co.jakelee.pixelbookshop.ui.travel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is travel Fragment"
    }
    val text: LiveData<String> = _text
}