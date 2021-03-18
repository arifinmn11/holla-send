package com.enigma.application.data.model.dashboard

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseDashboard(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("waiting")
	val waiting: Int? = null,

	@field:SerializedName("assigned")
	val assigned: Int? = null,

	@field:SerializedName("delivered")
	val delivered: Int? = null,

	@field:SerializedName("pickedUp")
	val pickedUp: Int? = null
) : Parcelable
