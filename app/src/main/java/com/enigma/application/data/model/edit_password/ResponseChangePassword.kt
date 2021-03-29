package com.enigma.application.presentation.profile_edit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseChangePassword(

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
data class UserDetails(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("identityCategory")
	val identityCategory: String? = null,

	@field:SerializedName("contactNumber")
	val contactNumber: String? = null,

	@field:SerializedName("identificationNumber")
	val identificationNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userDetails")
	val userDetails: UserDetails? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
