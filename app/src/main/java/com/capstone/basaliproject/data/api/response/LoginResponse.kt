package com.capstone.basaliproject.data.api.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

sealed class LoginResponse<out T : Any> {
	data class Success<out T : Any>(val data: T) : LoginResponse<T>()
	data class Error(val error: Msg?) : LoginResponse<Nothing>()
}
@Parcelize
data class SuccessResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

) : Parcelable

@Parcelize
data class FailureResponse(
	@field:SerializedName("msg")
	val msg: Msg? = null
) : Parcelable

@Parcelize
data class Msg(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("customData")
	val customData: CustomData? = null
) : Parcelable

@Parcelize
data class CustomData(
	val any: String? = null
) : Parcelable
