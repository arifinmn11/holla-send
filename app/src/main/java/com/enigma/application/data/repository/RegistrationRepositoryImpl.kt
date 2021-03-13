package com.enigma.application.data.repository

import com.enigma.application.data.api.RegistrationApi
import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.ResponseRegister
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(val registrationApi: RegistrationApi) :
    RegistrationRepository {
    override suspend fun postRegistration(requestRegister: RequestRegister): ResponseRegister =
        registrationApi.postRegistration(requestRegister)
}