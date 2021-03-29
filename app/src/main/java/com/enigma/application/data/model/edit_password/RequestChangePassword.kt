package com.enigma.application.data.model.edit_password

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestChangePassword(

	@field:SerializedName("oldPassword")
	val oldPassword: String? = null,

	@field:SerializedName("newPassword")
	val newPassword: String? = null
) : Parcelable
