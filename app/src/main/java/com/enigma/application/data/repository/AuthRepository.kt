package com.enigma.application.data.repository

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.model.radius.ResponseRadius

interface AuthRepository {
    suspend fun postAuth(requestAuth: RequestAuth): ResponseAuth
    suspend fun getMe(): ResponseProfile
    suspend fun getRadius() : ResponseRadius
}