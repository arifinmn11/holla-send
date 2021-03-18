package com.enigma.application.data.model.courier_activity.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseMyTasks(
	val code: Int? = null,
	val data: Data? = null,
	val message: String? = null,
	val timestamp: String? = null
) : Parcelable

@Parcelize
data class Data(
	val date: String? = null,
	val courier: Courier? = null,
	val leavingTime: String? = null,
	val taskList: List<TaskListItem?>? = null,
	val id: String? = null,
	val returnTime: String? = null
) : Parcelable

@Parcelize
data class TaskListItem(
	val notes: String? = null,
	val courierActivity: CourierActivity? = null,
	val courier: Courier? = null,
	val destination: Destination? = null,
	val pickUpTime: String? = null,
	val requestBy: RequestBy? = null,
	val id: String? = null,
	val deliveredTime: String? = null,
	val priority: String? = null,
	val status: String? = null,
	val createDate: String? = null
) : Parcelable

@Parcelize
data class RequestBy(
	val role: String? = null,
	val id: String? = null,
	val userDetails: UserDetails? = null,
	val email: String? = null,
	val username: String? = null
) : Parcelable

@Parcelize
data class CourierActivity(
	val date: String? = null,
	val courier: Courier? = null,
	val leavingTime: String? = null,
	val id: String? = null,
	val returnTime: String? = null
) : Parcelable

@Parcelize
data class Courier(
	val role: String? = null,
	val id: String? = null,
	val userDetails: UserDetails? = null,
	val email: String? = null,
	val username: String? = null
) : Parcelable

@Parcelize
data class UserDetails(
	val firstName: String? = null,
	val lastName: String? = null,
	val identityCategory: String? = null,
	val contactNumber: String? = null,
	val identificationNumber: String? = null,
	val id: String? = null
) : Parcelable

@Parcelize
data class Destination(
	val address: String? = null,
	val name: String? = null,
	val lon: Int? = null,
	val id: String? = null,
	val lat: Int? = null
) : Parcelable
