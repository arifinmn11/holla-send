package com.enigma.application.data.model.edit_profile

import com.google.gson.annotations.SerializedName

data class ResponseProfile(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

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
)
