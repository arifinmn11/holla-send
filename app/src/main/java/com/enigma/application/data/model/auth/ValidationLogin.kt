package com.enigma.application.data.model.auth

import com.google.gson.annotations.SerializedName

data class ValidationLogin(
    @field:SerializedName("username")
    val username: Boolean,

    @field:SerializedName("password")
    val password: Boolean
)