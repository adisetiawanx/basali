package com.capstone.basaliproject.data.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: ProfileData? = null
)

data class Photo(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class ProfileData(

	@field:SerializedName("isVerified")
	val isVerified: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: Photo? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("verificationCode")
	val verificationCode: Int? = null
)
