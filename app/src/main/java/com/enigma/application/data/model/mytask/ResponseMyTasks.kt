package com.enigma.application.data.model.mytask

import com.google.gson.annotations.SerializedName

data class ResponseMyTasks(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
)

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
)

data class CourierActivity(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("courier")
    val courier: Courier? = null,

    @field:SerializedName("leavingTime")
    val leavingTime: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("returnTime")
    val returnTime: String? = null
)

data class DataItem(

    @field:SerializedName("notes")
    val notes: String? = null,

    @field:SerializedName("courierActivity")
    val courierActivity: CourierActivity? = null,

    @field:SerializedName("courier")
    val courier: Courier? = null,

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
)

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
)

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
)

data class Courier(

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
