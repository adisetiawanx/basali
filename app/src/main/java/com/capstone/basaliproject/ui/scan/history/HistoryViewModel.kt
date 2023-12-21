package com.capstone.basaliproject.ui.scan.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.data.api.response.HistoryResponse
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _aksaraData = MutableLiveData<List<DataItem>>()
    val aksaraData: LiveData<List<DataItem>> = _aksaraData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var searchQuery: String? = null

    fun searchHistory(query: String?){
        searchQuery = query
        getHistory()
    }

    init {
        getHistory()
    }

    private fun getHistory(){
        _isLoading.value = true
        val auth = FirebaseAuth.getInstance()
        val user  = auth.currentUser
        if (user != null) {
            _isLoading.value = true

            user.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.token
                    token?.let { data ->
                        val apiService = ApiConfig.getApiService(data)

                        val call = apiService.getHistory(searchQuery ?: "")
                        call.enqueue(object : Callback<HistoryResponse> {
                            override fun onResponse(
                                call: Call<HistoryResponse>,
                                response: Response<HistoryResponse>
                            ) {
                                _isLoading.value = false
                                if (response.isSuccessful){
                                    val responseBody = response.body()
                                    if (responseBody != null){
                                        val reverse = responseBody.data.reversed()
                                        val sortedData = reverse.sortedByDescending { it.scannedAt?.month }

                                        _aksaraData.value = sortedData
                                    }
                                }else{
                                    Log.e("HistoryViewModel","onFailure: ${response.message()}")
                                }
                            }
                            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                                _isLoading.value = false
                                Log.e("HistoryViewModel", "onFailure: ${t.message}")
                            }
                        })
                    }
                } else {
                    _isLoading.value = false
                    Log.e("HistoryViewModel", "Failed to get token: ${task.exception?.message}")
                }
            }
        } else {
            _isLoading.value = false
            Log.e("HistoryViewModel", "User is null.")
        }
    }
}