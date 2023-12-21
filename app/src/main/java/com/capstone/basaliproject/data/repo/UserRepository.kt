package com.capstone.basaliproject.data.repo

import com.capstone.basaliproject.data.api.response.EditProfilePictureResponse
import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.Photo
import com.capstone.basaliproject.data.api.response.ProfileResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.pref.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class UserRepository private constructor(
//    private val pref: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(raw: RequestBody): RegisterResponse {
        return apiService.register(raw)
    }

    suspend fun login(raw: RequestBody): LoginResponse {
        return apiService.login(raw)
    }

    suspend fun editProfilePicture(image: MultipartBody.Part): Response<EditProfilePictureResponse> {
        return apiService.editProfilePicture(image)
    }

    suspend fun deleteProfilePicture(): Response<EditProfilePictureResponse> {
        return apiService.deleteProfilePicture()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            pref: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }

        fun resetInstance() {
            instance = null
        }
    }
}