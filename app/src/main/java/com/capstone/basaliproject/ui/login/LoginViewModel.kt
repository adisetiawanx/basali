package com.capstone.basaliproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.basaliproject.data.api.response.ErrorResponse
import com.capstone.basaliproject.data.api.response.SuccessResponse
import com.capstone.basaliproject.data.pref.UserModel
import com.capstone.basaliproject.data.repo.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception

class LoginViewModel(
    private val repository: UserRepository
): ViewModel() {
    private var _result = MutableLiveData<SuccessResponse>()
    val result : LiveData<SuccessResponse> =_result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun login(raw: RequestBody) {
        try {
            _isLoading.value = true
            val message = repository.login(raw)
            _result.value = message
        } catch (e: HttpException) {
            // Get error message
            val jsonInString = e.response()?.errorBody()?.string()
            try {
                // Try to parse the error response as JSON
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Unknown error"
                _result.value = SuccessResponse(null)
            } catch (jsonException: JsonSyntaxException) {
                // If parsing as JSON fails, treat the error response as a plain string
                _result.value = SuccessResponse(null)
            } catch (otherException: Exception) {
                // Handle other exceptions that might occur during parsing
                _result.value = SuccessResponse(null)
            }
        } catch (e : Exception) {
            _result.value = SuccessResponse(null)
        } finally {
            _isLoading.value = false
        }
    }

    fun getSession() = runBlocking {
        repository.getSession().first()
    }
}