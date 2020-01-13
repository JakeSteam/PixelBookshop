package uk.co.jakelee.pixelbookshop.ui.customise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomiseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is customise Fragment"
    }
    val text: LiveData<String> = _text
}