package com.enigma.application.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseAuth(
    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("token")
    val token: String? = null
) : Parcelable
