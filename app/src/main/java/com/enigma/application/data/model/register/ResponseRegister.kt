package com.enigma.application.data.model.register

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseRegister(

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

    @field:SerializedName("firstName")
    val firstName: String,

    @field:SerializedName("lastName")
    val lastName: String,

    @field:SerializedName("identityCategory")
    val identityCategory: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("identificationNumber")
    val identificationNumber: String,

    @field:SerializedName("id")
    val id: String
) : Parcelable
