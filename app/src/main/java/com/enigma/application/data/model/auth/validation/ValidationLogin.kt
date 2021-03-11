package com.enigma.application.data.model.auth.validation

import com.google.gson.annotations.SerializedName

data class ValidationLogin(
    @field:SerializedName("email")
    val email: Boolean,

    @field:SerializedName("password")
    val password: Boolean
)