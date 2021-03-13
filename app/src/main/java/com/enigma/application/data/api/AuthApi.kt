package com.enigma.application.data.api

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("authenticate")
    suspend fun postAuth(
        @Body requestAuth: RequestAuth
    ): ResponseAuth

    @GET("/user/me")
    suspend fun getProfile(): ResponseProfile
}