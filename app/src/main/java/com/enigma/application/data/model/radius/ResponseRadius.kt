package com.enigma.application.data.model.radius

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseRadius(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable
