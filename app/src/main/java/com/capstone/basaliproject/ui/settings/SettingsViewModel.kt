package com.capstone.basaliproject.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.basaliproject.data.api.response.UpdatePhotoResponse
import com.capstone.basaliproject.data.repo.UserRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text

    // LiveData to hold the UpdatePhotoResponse
    private val _updatePhotoResponse = MutableLiveData<UpdatePhotoResponse>()
    val updatePhotoResponse: LiveData<UpdatePhotoResponse> = _updatePhotoResponse

    // Function to update the profile photo
    fun updateProfilePhoto(raw: RequestBody) {
        viewModelScope.launch {
            try {
                val response = userRepository.updateProfilePhoto(raw)
                _updatePhotoResponse.value = response
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }
}
