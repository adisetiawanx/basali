package com.capstone.basaliproject.data.api.response

import com.google.gson.annotations.SerializedName

data class PutProfilePictureResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: PutPictureData? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class PutPictureData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
