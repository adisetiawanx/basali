package com.capstone.basaliproject.data.api.retrofit

import com.capstone.basaliproject.data.api.response.ConfirmationResponse
import com.capstone.basaliproject.data.api.response.HistoryResponse
import com.capstone.basaliproject.data.api.response.LoginResponse
import com.capstone.basaliproject.data.api.response.Photo
import com.capstone.basaliproject.data.api.response.ProfileData
import com.capstone.basaliproject.data.api.response.ProfileResponse
import com.capstone.basaliproject.data.api.response.RegisterResponse
import com.capstone.basaliproject.data.api.response.ScanResultResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body raw: RequestBody
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body raw: RequestBody
    ): LoginResponse

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

    @GET("/api/scan/aksara")
    fun getHistory(
        @Query("q") query: String
    ): Call<HistoryResponse>

    @GET("/api/user/profile")
    fun getProfile(): Call<ProfileResponse>

}