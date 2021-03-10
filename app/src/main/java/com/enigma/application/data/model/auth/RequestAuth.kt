package com.enigma.application.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestAuth(
    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable