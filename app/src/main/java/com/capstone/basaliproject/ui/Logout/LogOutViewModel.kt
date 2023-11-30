package com.capstone.basaliproject.ui.Logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.data.repo.UserRepository
import kotlinx.coroutines.launch

class LogOutViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}