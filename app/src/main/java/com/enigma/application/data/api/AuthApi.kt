package com.enigma.application.data.api

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.model.radius.ResponseRadius
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


    @GET("/offset")
    suspend fun getRadius(): ResponseRadius
}