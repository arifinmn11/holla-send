package com.enigma.application.data.model

import android.os.Parcelable
import com.enigma.application.data.model.profile.Data
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ResponseValidation(
    @field:SerializedName("status")
    var status: String? = null,


    @field:SerializedName("message")
    var message: String? = null
) : Parcelable