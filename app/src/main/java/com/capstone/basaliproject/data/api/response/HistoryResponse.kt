package com.capstone.basaliproject.data.api.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
	val scannedAt: ScannedAt? = null,

	@field:SerializedName("imgaeUrl")
	val imgaeUrl: String? = null,

	@PrimaryKey
	@field:SerializedName("predictionId")
	val predictionId: String
)

@Parcelize
data class ScannedAt(

	@field:SerializedName("hour")
	val hour: Int? = null,

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
) : Parcelable
