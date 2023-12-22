package com.capstone.basaliproject.ui.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogOutViewModel : ViewModel() {

    private val _isLogOut = MutableLiveData<Boolean>()
    val isLogOut: LiveData<Boolean> = _isLogOut
}