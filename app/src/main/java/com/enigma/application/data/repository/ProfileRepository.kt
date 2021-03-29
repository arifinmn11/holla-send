package com.enigma.application.data.repository

import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.ResponseProfile

interface ProfileRepository {
    suspend fun updateProfile(data: RequestProfile): ResponseProfile
}