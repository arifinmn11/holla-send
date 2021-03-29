package com.enigma.application.presentation.profile_edit

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.ResponseValidation
import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.data.repository.ProfileRepository
import com.enigma.application.data.model.edit_profile.ResponseProfile as ResponseEditProfile
import com.enigma.application.di.qualifier.GetProfile
import com.enigma.application.di.qualifier.Profile
import com.enigma.application.di.qualifier.ValidationAuth
import com.enigma.application.utils.Constants
import com.wajahatkarim3.easyvalidation.core.view_ktx.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ViewModelChangeProfile @Inject constructor(
    @GetProfile val repository: AuthRepository,
    @Profile val repoProfile: ProfileRepository
) :
    ViewModel() {

    private val _liveValidation: MutableLiveData<ResponseValidation> = MutableLiveData()

    fun getValidation(): LiveData<ResponseValidation> {
        return _liveValidation
    }


    fun getProfile() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseProfile? = null
            try {
                response = repository.getMe()
            } catch (e: Exception) {
                response =
                    ResponseProfile(
                        code = 400,
                        data = null,
                        message = "Something wrong with your connection!",
                    )
            } finally {
                emit(response)
            }
        }
    }

    fun updateProfile(request: RequestProfile) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(5000) {
                var response: ResponseEditProfile? = null
                try {
                    response = repoProfile.updateProfile(request)
                } catch (e: Exception) {
                    response =
                        ResponseEditProfile(
                            code = 400,
                            data = null,
                            message = "Something wrong with your connection!",
                        )
                } finally {
                    emit(response)
                }
            }
        }

    fun checkValidation(
        firstName: String,
        lastName: String,
        identification: String,
        noIdentification: String,
        contactNumber: String
    ) {
        var validation = ResponseValidation("", "")
        if (!firstName.noSpecialCharacters() || !firstName.noNumbers() || identification.minLength(5)) {
            validation.message = "First name is not valid!"
            validation.status = Constants.VALIDATION_FIRSTNAME
        } else if (lastName.isEmpty()) {
            validation.message = "Last name is not valid!"
            validation.status = Constants.VALIDATION_LASTNAME
        } else if (identification.isEmpty()) {
            validation.message = "Identification is not valid!"
            validation.status = Constants.VALIDATION_IDENTIFICATION
        } else if (noIdentification.length != 16) {
            Log.d("identification", noIdentification.length.toString())
            validation.message = "No Identification name is not valid!"
            validation.status = Constants.VALIDATION_NO_IDENTIFICATION
        } else if (contactNumber.noNumbers() || contactNumber.length <= 6) {
            validation.message = "Contact Number is not valid!"
            validation.status = Constants.VALIDATION_ADDRESS
        } else {
            validation.message = ""
            validation.status = Constants.VALIDATION_SUCCESS
        }
        _liveValidation.postValue(validation)
    }
}