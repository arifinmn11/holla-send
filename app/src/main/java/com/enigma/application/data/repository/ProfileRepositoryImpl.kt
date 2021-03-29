package com.enigma.application.data.repository

import com.enigma.application.data.api.ProfileApi
import com.enigma.application.data.model.edit_password.RequestChangePassword
import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.ResponseProfile
import com.enigma.application.presentation.profile_edit.ResponseChangePassword
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(val repository: ProfileApi) : ProfileRepository {
    override suspend fun updateProfile(request: RequestProfile): ResponseProfile =
        repository.updateProfile(request)

    override suspend fun updatePassword(request: RequestChangePassword): ResponseChangePassword =
        repository.changePassword(request)
}