package com.enigma.application.data.model.register

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("userDetails")
	val userDetails: UserDetails
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable

@Parcelize
data class UserDetails(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("identityCategory")
	val identityCategory: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("identificationNumber")
	val identificationNumber: String
) : Parcelable
