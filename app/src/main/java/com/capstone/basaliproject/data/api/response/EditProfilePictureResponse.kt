package com.capstone.basaliproject.data.api.response

import com.google.gson.annotations.SerializedName

data class EditProfilePictureResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: EditPictureData? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class EditPictureData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
