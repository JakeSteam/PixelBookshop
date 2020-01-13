package uk.co.jakelee.pixelbookshop.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "123456789 xp"
    }
    val text: LiveData<String> = _text
}