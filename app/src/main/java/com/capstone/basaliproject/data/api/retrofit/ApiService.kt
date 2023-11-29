package com.capstone.basaliproject.data.api.retrofit

import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.SuccessResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body raw: RequestBody
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body raw: RequestBody
    ): SuccessResponse
}