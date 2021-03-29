package com.enigma.application.data.repository

import com.enigma.application.data.model.edit_password.RequestChangePassword
import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.ResponseProfile
import com.enigma.application.presentation.profile_edit.ResponseChangePassword

interface ProfileRepository {
    suspend fun updateProfile(request: RequestProfile): ResponseProfile
    suspend fun updatePassword(request: RequestChangePassword): ResponseChangePassword
}