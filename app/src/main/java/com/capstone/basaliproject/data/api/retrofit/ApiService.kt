package com.capstone.basaliproject.data.api.retrofit

import com.capstone.basaliproject.data.api.response.ConfirmationResponse
import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.ScanResultResponse
import com.capstone.basaliproject.data.api.response.UpdatePhotoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part


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

    @PATCH("/api/auth/verify-email")
    suspend fun verifyEmail(
        @Body raw: RequestBody
    ): ConfirmationResponse

    @PATCH("/api/auth/resend-code")
    suspend fun resendCode(): ConfirmationResponse

    @Multipart
    @POST("/api/scan/aksara")
    suspend fun postImageToScan(
        @Part file: MultipartBody.Part
    ): ScanResultResponse
}