package com.capstone.basaliproject.ui.confirm

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ConfirmationViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> = _navigateToMain

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun verifyEmail(verifCode: String) {
        _isLoading.value = true

        // Create a JSONObject with your verification code
        val json = JSONObject()
        json.put("code", verifCode)

        // Convert the JSONObject to a RequestBody
        val body: RequestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        // Get an instance of your ApiService
        val user = Firebase.auth.currentUser
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                val apiService = idToken?.let { it1 -> ApiConfig.getApiService(it1) }

                // Call the verifyEmail function
                CoroutineScope(Dispatchers.IO).launch {
                    val response = apiService?.verifyEmail(body)

                    withContext(Dispatchers.Main) {
                        _isLoading.value = false

                        if (response != null) {
                            if (response.userId != null) {
                                _isSuccess.value = true
                                _navigateToMain.value = true
                                // Handle successful response
                                val confirmationResponse = response.msg
                                Log.d(
                                    "ConfirmationActivity",
                                    "Email successfully verified: ${confirmationResponse}"
                                )
                            } else {
                                _isSuccess.value = false
                                // Handle error
                                Log.e("ConfirmationActivity", "Failed to verify email")
                            }
                        }
                    }
                }
            } else {
                _isSuccess.value = false
                Log.w(TAG, "Failed to get ID Token", task.exception)
            }
        }
    }

    fun doneNavigating() {
        _navigateToMain.value = false
    }
}
