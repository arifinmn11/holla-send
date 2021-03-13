package com.enigma.application.presentation.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enigma.application.data.model.ResponseValidation
import com.enigma.application.utils.Constans
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    val _liveValidation: MutableLiveData<ResponseValidation> = MutableLiveData()

    fun getValidation(): LiveData<ResponseValidation> {
        return _liveValidation
    }

    fun checkValidation(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) {
        var validation = ResponseValidation("", "")
        if (!email.validEmail()) {
            validation.message = "Emial is not Valid"
            validation.status = Constans.VALIDATION_EMAIL
        } else if (username.length <= 6) {
            validation.message = "Password is not Valid min 6 character"
            validation.status = Constans.VALIDATION_USERNAME
        } else if (password.length <= 6) {
            validation.message = "Password is not Valid min 6 character"
            validation.status = Constans.VALIDATION_PASSWORD
        } else if (password == confirmPassword) {
            validation.message = "Confirmation Password is not valid!"
            validation.status = Constans.VALIDATION_CONFIRM_PASSWORD
        } else {
            validation.message = ""
            validation.status = Constans.VALIDATION_SUCCESS
        }
        _liveValidation.postValue(validation)
    }

}