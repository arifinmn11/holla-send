package com.enigma.application.data.model.mytask


import com.google.gson.annotations.SerializedName

data class ResponseMyTask(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: DataItem? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: String? = null
)