package com.capstone.basaliproject.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.basaliproject.data.api.response.EditProfilePictureResponse
import com.capstone.basaliproject.data.api.response.ProfileResponse
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _profileData = MutableLiveData<ProfileResponse?>()
    val profileData: LiveData<ProfileResponse?> get() = _profileData

    private val _editProfilePictureResponse = MutableLiveData<EditProfilePictureResponse>()
    private val _deleteProfilePictureResponse = MutableLiveData<EditProfilePictureResponse>()

    private suspend fun getApiServiceWithToken(): ApiService? {
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

    fun getProfileData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiService = getApiServiceWithToken()
                val call: Call<ProfileResponse>? = apiService?.getProfile()

                call?.let {
                    val response = executeDeferred(call)
                    _profileData.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: $e")
            }
        }
    }

    suspend fun editProfilePicture(image: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiService = getApiServiceWithToken()
                val response = apiService?.editProfilePicture(image)

                if (response != null) {
                    if (response.isSuccessful) {
                        _editProfilePictureResponse.postValue(response.body())
                    } else {
                        Log.e("HomeViewModel", "${!response.isSuccessful}")
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: $e")
            }
        }
    }

    suspend fun deleteProfilePicture() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiService = getApiServiceWithToken()
                val response = apiService?.deleteProfilePicture()

                if (response != null) {
                    if (response.isSuccessful) {
                        _deleteProfilePictureResponse.postValue(response.body())
                    } else {
                        Log.e("HomeViewModel", "${!response.isSuccessful}")
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: $e")
            }
        }
    }


    private suspend fun <T> executeDeferred(call: Call<T>): T? = suspendCancellableCoroutine { continuation ->
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body())
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })

        continuation.invokeOnCancellation {
            call.cancel()
        }
    }
}