package com.enigma.application.data.model.newtask

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseNewTaskWaiting(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
) : Parcelable

@Parcelize
data class Destination(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
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
data class RequestBy(

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

@Parcelize
data class DataItem(

    @field:SerializedName("notes")
    val notes: String? = null,

    @field:SerializedName("courier")
    val courier: String? = null,

    @field:SerializedName("destination")
    val destination: Destination? = null,

    @field:SerializedName("pickUpTime")
    val pickUpTime: String? = null,

    @field:SerializedName("requestBy")
    val requestBy: RequestBy? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("deliveredTime")
    val deliveredTime: String? = null,

    @field:SerializedName("priority")
    val priority: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("createDate")
    val createDate: String? = null

) : Parcelable
