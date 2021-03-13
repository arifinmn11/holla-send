package com.enigma.application.data.repository

import com.enigma.application.data.api.AuthApi
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(val authApi: AuthApi) : AuthRepository {
    override suspend fun postAuth(requestAuth: RequestAuth): ResponseAuth =
        authApi.postAuth(requestAuth)

    override suspend fun getMe(): ResponseProfile = authApi.getProfile()

}