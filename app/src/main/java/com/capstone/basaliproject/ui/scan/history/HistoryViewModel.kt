package com.capstone.basaliproject.ui.scan.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.repo.UserRepository
import com.capstone.basaliproject.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HistoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun getPagingData(): LiveData<PagingData<DataItem>> {
        return repository.getAksaraSortedByScannedAt()
    }

    suspend fun getApiServiceWithToken(): ApiService? {
        val auth = FirebaseAuth.getInstance()
        val user  = auth.currentUser
        return if (user != null) {
            val token = withContext(Dispatchers.IO) {
                user.getIdToken(true).await().token
            }
            token?.let {
                ApiConfig.getApiService(it)
            }
        } else {
            null
        }
    }


}