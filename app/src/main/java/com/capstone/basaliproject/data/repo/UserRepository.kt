package com.capstone.basaliproject.data.repo

import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.UpdatePhotoResponse
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.pref.UserPreference
 import okhttp3.RequestBody

class UserRepository private constructor(
    private val pref: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(raw: RequestBody): RegisterResponse {
        return apiService.register(raw)
    }

    suspend fun login(raw: RequestBody): LoginResponse {
        return apiService.login(raw)
    }

    suspend fun updateProfilePhoto(raw: RequestBody): UpdatePhotoResponse {
        return apiService.updateProfilePhoto(raw)
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            pref: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(pref, apiService)
            }.also { instance = it }

        fun resetInstance() {
            instance = null
        }
    }
}