package com.enigma.application.data.api

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/user")
    suspend fun postAuth(
        @Body requestAuth: RequestAuth
    ): ResponseAuth

}