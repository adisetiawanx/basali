package com.capstone.basaliproject.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.data.api.response.ErrorResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.repo.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.RequestBody
import retrofit2.HttpException
import java.lang.Exception

class SignupViewModel(
    private val repository: UserRepository
): ViewModel() {
    private var _result = MutableLiveData<RegisterResponse>()
    val result : LiveData<RegisterResponse> =_result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    suspend fun register(raw: RequestBody) {
        try {
            _isLoading.value = true
            //get success message
            val message = repository.register(raw)
            _result.value = message
        } catch (e: HttpException) {
            // Get error message
            val jsonInString = e.response()?.errorBody()?.string()
            try {
                // Try to parse the error response as JSON
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Unknown error"
                _result.value = RegisterResponse("failed", errorMessage)
            } catch (jsonException: JsonSyntaxException) {
                // If parsing as JSON fails, treat the error response as a plain string
                _result.value = RegisterResponse("failed", jsonInString ?: "Unknown error")
            } catch (otherException: Exception) {
                // Handle other exceptions that might occur during parsing
                _result.value = RegisterResponse("failed", "Error parsing response")
            }
        } catch (e : Exception) {
            _result.value = RegisterResponse(e.message.toString())
        } finally {
            _isLoading.value = false
        }
    }
}