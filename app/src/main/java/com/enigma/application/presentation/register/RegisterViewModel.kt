package com.enigma.application.presentation.register

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.ResponseValidation
import com.enigma.application.data.model.register.RequestRegister
import com.enigma.application.data.model.register.ResponseRegister
import com.enigma.application.data.repository.RegistrationRepository
import com.enigma.application.di.qualifier.PostRegistration
import com.enigma.application.utils.Constants
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(@PostRegistration val repository: RegistrationRepository) :
    ViewModel() {
    val _liveValidation: MutableLiveData<ResponseValidation> = MutableLiveData()

    fun getValidation(): LiveData<ResponseValidation> {
        return _liveValidation
    }

    fun postRegister(requestRegister: RequestRegister) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(5000) {
                var response: ResponseRegister? = null
                try {
                    response = repository.postRegistration(requestRegister)
                } catch (e: Exception) {
                    response =
                        ResponseRegister(
                            code = 400,
                            data = null,
                            message = "Email or Password not available!",
                        )
                } finally {
                    response?.message?.let { Log.d("MESSAGE", it) }
                    emit(response)
                }
            }
        }

    fun checkValidation(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        identification: String,
        noIdentification: String,
        address: String
    ) {
        var validation = ResponseValidation("", "")
        if (!email.validEmail()) {
            validation.message = "Emial is not Valid"
            validation.status = Constants.VALIDATION_EMAIL
        } else if (username.length < 6 && username.length > 12) {
            validation.message = "Password is not Valid min 6 character"
            validation.status = Constants.VALIDATION_USERNAME
        } else if (password.length < 6 && username.length > 20) {
            validation.message = "Password is not Valid min 6 character"
            validation.status = Constants.VALIDATION_PASSWORD
        } else if (password != confirmPassword) {
            validation.message = "Confirmation Password is not valid!"
            validation.status = Constants.VALIDATION_CONFIRM_PASSWORD
        } else if (firstName.isEmpty()) {
            validation.message = "First name is not valid!"
            validation.status = Constants.VALIDATION_FIRSTNAME
        } else if (lastName.isEmpty()) {
            validation.message = "Last name is not valid!"
            validation.status = Constants.VALIDATION_LASTNAME
        } else if (identification.isEmpty()) {
            validation.message = "Identification is not valid!"
            validation.status = Constants.VALIDATION_IDENTIFICATION
        } else if (noIdentification.isEmpty()) {
            validation.message = "No Identification name is not valid!"
            validation.status = Constants.VALIDATION_NO_IDENTIFICATION
        } else if (address.isEmpty()) {
            validation.message = "Address is not valid!"
            validation.status = Constants.VALIDATION_ADDRESS
        } else {
            validation.message = ""
            validation.status = Constants.VALIDATION_SUCCESS
        }
        _liveValidation.postValue(validation)
    }

}