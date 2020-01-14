package uk.co.jakelee.pixelbookshop.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatusViewModel : ViewModel() {

    private val _level = MutableLiveData<String>().apply {
        value = "123456789 xp"
    }
    val level: LiveData<String> = _level

    private val _xp = MutableLiveData<String>().apply {
        value = "123456789 xp"
    }
    val xp: LiveData<String> = _xp

    private val _stock = MutableLiveData<String>().apply {
        value = "123456789 xp"
    }
    val stock: LiveData<String> = _stock

    private val _coins = MutableLiveData<String>().apply {
        value = "123456789 xp"
    }
    val coins: LiveData<String> = _coins
}