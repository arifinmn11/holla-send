package com.enigma.application.data.repository

import com.enigma.application.data.api.AuthApi
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.model.radius.ResponseRadius
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(val repository: AuthApi) : AuthRepository {
    override suspend fun postAuth(requestAuth: RequestAuth): ResponseAuth =
        repository.postAuth(requestAuth)

    override suspend fun getMe(): ResponseProfile =
        repository.getProfile()

    override suspend fun getRadius(): ResponseRadius =
        repository.getRadius()

}