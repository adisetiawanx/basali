package com.capstone.basaliproject.data.api.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList()
)

@Entity(tableName = "history")
data class DataItem(

	@field:SerializedName("predictionResult")
	val predictionResult: String? = null,

	@field:SerializedName("scannedAt")
	val scannedAt: String? = null,

	@field:SerializedName("imgaeUrl")
	val imgaeUrl: String? = null,

	@PrimaryKey
	@field:SerializedName("predictionId")
	val predictionId: String
)
