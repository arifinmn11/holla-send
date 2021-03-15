package com.enigma.application.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAuth(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("role")
    val role: String

) : Parcelable
