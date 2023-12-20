package com.capstone.basaliproject.data.di

import android.content.Context
import com.capstone.basaliproject.data.api.retrofit.ApiConfig
import com.capstone.basaliproject.data.database.HistoryDatabase
import com.capstone.basaliproject.data.pref.UserPreference
import com.capstone.basaliproject.data.pref.dataStore
import com.capstone.basaliproject.data.repo.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        val historyDatabase = HistoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(historyDatabase, pref, apiService)
    }

    fun resetIntance() {
        UserRepository.resetInstance()
    }
}