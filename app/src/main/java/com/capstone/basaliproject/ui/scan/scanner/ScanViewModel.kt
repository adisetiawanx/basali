package com.capstone.basaliproject.ui.scan.scanner

import android.net.Uri
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

class ScanViewModel : ViewModel() {
    private var currentImageUri: Uri? = null

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

    fun saveInstanceState(imageUri: Uri?) {
        currentImageUri = imageUri
    }
}