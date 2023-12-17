package com.capstone.basaliproject.data.repo

import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(raw: RequestBody): RegisterResponse {
        return apiService.register(raw)
    }

    suspend fun login(raw: RequestBody): LoginResponse {
        return apiService.login(raw)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }

        fun resetInstance() {
            instance = null
        }
    }
}