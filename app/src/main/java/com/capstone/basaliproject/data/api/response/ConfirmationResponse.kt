package com.capstone.basaliproject.data.api.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ConfirmationResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
) : Parcelable
