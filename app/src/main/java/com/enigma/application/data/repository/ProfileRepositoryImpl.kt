package com.enigma.application.data.repository

import com.enigma.application.data.api.ProfileApi
import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.ResponseProfile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(val repository: ProfileApi) : ProfileRepository {
    override suspend fun updateProfile(request: RequestProfile): ResponseProfile =
    repository.updateProfile(request)
}