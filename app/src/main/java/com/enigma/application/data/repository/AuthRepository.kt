package com.enigma.application.data.repository

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth

interface AuthRepository {
    suspend fun postAuth(requestAuth: RequestAuth): ResponseAuth
}