package com.capstone.basaliproject.data.api.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ErrorLoginResponse(

	@field:SerializedName("msg")
	val msg: Msg? = null
) : Parcelable
