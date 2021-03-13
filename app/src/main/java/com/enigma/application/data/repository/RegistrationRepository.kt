package com.enigma.application.data.repository

import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.ResponseRegister

interface RegistrationRepository {
    suspend fun postRegistration(requestRegister: RequestRegister): ResponseRegister
}