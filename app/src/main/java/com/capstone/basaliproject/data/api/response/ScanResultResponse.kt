package com.capstone.basaliproject.data.api.response

import com.google.gson.annotations.SerializedName

data class ScanResultResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("userId")
	val userId: String
)

data class Data(

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null
)
