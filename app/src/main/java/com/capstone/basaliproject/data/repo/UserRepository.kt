package com.capstone.basaliproject.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.UpdatePhotoResponse
import com.capstone.basaliproject.data.api.retrofit.ApiService
import com.capstone.basaliproject.data.database.HistoryDatabase
import com.capstone.basaliproject.data.pref.UserPreference
import com.capstone.basaliproject.data.remote.HistoryRemoteMediator
import okhttp3.RequestBody

class UserRepository private constructor(
    private val dataDatabase: HistoryDatabase,
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

    @OptIn(ExperimentalPagingApi::class)
    fun getAksaraSortedByScannedAt(): LiveData<PagingData<DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = HistoryRemoteMediator(dataDatabase, apiService),
            pagingSourceFactory = {
                dataDatabase.historyDao().getAllHistory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            dataDatabase: HistoryDatabase,
            pref: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(dataDatabase, pref, apiService)
            }.also { instance = it }

        fun resetInstance() {
            instance = null
        }
    }
}