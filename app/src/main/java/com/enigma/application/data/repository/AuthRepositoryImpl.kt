package com.enigma.application.data.repository

import com.enigma.application.data.api.RetrofitInstance
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth

class AuthRepositoryImpl : AuthRepository {
    override suspend fun postAuth(requestAuth: RequestAuth): ResponseAuth =
        RetrofitInstance.authApi.postAuth(requestAuth)
}