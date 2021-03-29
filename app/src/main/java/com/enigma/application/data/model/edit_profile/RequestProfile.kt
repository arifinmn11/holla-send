package com.enigma.application.data.model.edit_profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestProfile(

	@field:SerializedName("userDetails")
	val userDetails: UserDetails? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
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
	val identificationNumber: String? = null
) : Parcelable
