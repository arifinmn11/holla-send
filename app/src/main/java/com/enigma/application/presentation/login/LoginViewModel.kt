package com.enigma.application.presentation.login

import android.util.Log
import androidx.lifecycle.*
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.auth.validation.ValidationLogin
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.di.qualifier.PostAuth
import com.enigma.application.utils.Constans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(@PostAuth val repository: AuthRepository) : ViewModel() {
    private val _validation: MutableLiveData<ValidationLogin> = MutableLiveData()

    fun getValidation(): LiveData<ValidationLogin> {
        return _validation
    }

    fun postAuth(requestAuth: RequestAuth) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(5000) {
                var response: ResponseAuth? = null
                try {
                    response = repository.postAuth(requestAuth)
                } catch (e: Exception) {

                    Log.d("RESPONSE", "$e")
                    response =
                        ResponseAuth(
                            code = 400,
                            message = "Error, try again",
                            data = null
                        )
                    emit(response)
                } finally {
                    emit(response)
                }

            }
        }

    fun validation(requestAuth: RequestAuth) {
        if (!requestAuth.email.matches(Constans.VALIDATION_EMAIL.toRegex())) {
            _validation.postValue(ValidationLogin(true, false))
        } else if (requestAuth.password.length <= 8) {
            _validation.postValue(ValidationLogin(false, true))
        } else {
            _validation.postValue(ValidationLogin(false, false))
        }
    }
}