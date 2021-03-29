package com.enigma.application.data.api

import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.ResponseProfile
import retrofit2.http.Body
import retrofit2.http.PUT

interface ProfileApi {

    @PUT("user")
    suspend fun updateProfile(
        @Body request: RequestProfile
    ): ResponseProfile

    @PUT("change-password")
    suspend fun changePassword(
        @Body request: RequestProfile
    ): ResponseProfile
}