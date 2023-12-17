package com.capstone.basaliproject.data.api.retrofit

import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.UpdatePhotoResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body raw: RequestBody
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body raw: RequestBody
    ): LoginResponse

    @PUT("/api/user/profile/photo")
    suspend fun updateProfilePhoto(
        @Body raw: RequestBody
    ): UpdatePhotoResponse
}