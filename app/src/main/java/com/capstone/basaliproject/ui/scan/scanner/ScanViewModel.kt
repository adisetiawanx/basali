package com.capstone.basaliproject.ui.scan.scanner

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.data.api.response.ScanResultResponse
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ScanViewModel() : ViewModel() {
    var currentImageUri: Uri? = null

    private val _scanImage = MutableLiveData<ScanResultResponse>()
    var scanImage: LiveData<ScanResultResponse> = _scanImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

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

    suspend fun postImage(apiService: ApiService, file: MultipartBody.Part) {
        _isLoading.value = true
        try {

            if (getApiServiceWithToken() != null) {
                val response = apiService.postImageToScan(file)
                _scanImage.postValue(response)
            }

        } catch (e: Exception) {
            Log.e("ScanViewModel", "Error posting image: ${e.message}")
        } finally {
            _isLoading.postValue(false)
        }
    }

    fun saveInstanceState(imageUri: Uri?) {
        currentImageUri = imageUri
    }
}