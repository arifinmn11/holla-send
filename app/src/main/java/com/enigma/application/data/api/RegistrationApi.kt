package com.enigma.application.data.api

import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.ResponseRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {
    @POST("/user/register")
    suspend fun postRegistration(
        @Body requestRegister: RequestRegister
    ): ResponseRegister
}