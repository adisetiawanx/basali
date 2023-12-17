package com.capstone.basaliproject.ui.logout

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.basaliproject.MainActivity
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LogOutViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLogOut = MutableLiveData<Boolean>()
    val isLogOut: LiveData<Boolean> = _isLogOut
}