package com.capstone.basaliproject.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.basaliproject.data.di.Injection
import com.capstone.basaliproject.data.repo.UserRepository
import com.capstone.basaliproject.ui.logout.LogOutViewModel
import com.capstone.basaliproject.ui.login.LoginViewModel
import com.capstone.basaliproject.ui.settings.SettingsViewModel
import com.capstone.basaliproject.ui.signup.SignupViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LogOutViewModel::class.java) -> {
                LogOutViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun refreshObject() {
            INSTANCE = null
            Injection.resetIntance()
        }
    }
}