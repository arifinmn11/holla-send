package com.enigma.application.presentation.password_edit

import androidx.lifecycle.*
import com.enigma.application.data.model.ResponseValidation
import com.enigma.application.data.model.edit_password.RequestChangePassword
import com.enigma.application.data.repository.ProfileRepository
import com.enigma.application.di.qualifier.Profile
import com.enigma.application.presentation.profile_edit.ResponseChangePassword
import com.enigma.application.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ViewModelChangePassword @Inject constructor(@Profile val repository: ProfileRepository) :
    ViewModel() {

    private val _liveValidation: MutableLiveData<ResponseValidation> = MutableLiveData()

    fun getValidation(): LiveData<ResponseValidation> {
        return _liveValidation
    }

    fun updatePasswordApi(request: RequestChangePassword) =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            withTimeout(5000) {
                var response: ResponseChangePassword? = null
                try {
                    response = repository.updatePassword(request)
                } catch (e: Exception) {
                    response =
                        ResponseChangePassword(
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
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {

        var validation = ResponseValidation("", "")
        if (oldPassword.length < 8) {
            validation.message = "old password is not valid!"
            validation.status = Constants.VALIDATION_PASSWORD
        } else if (newPassword.length < 8 || newPassword == oldPassword) {
            validation.message = "new password is not valid!"
            validation.status = Constants.VALIDATION_NEW_PASSWORD
        } else if (confirmPassword.length < 8 || confirmPassword != newPassword) {
            validation.message = "confirm password is not valid!"
            validation.status = Constants.VALIDATION_CONFIRM_PASSWORD
        } else {
            validation.message = ""
            validation.status = Constants.VALIDATION_SUCCESS
        }
        _liveValidation.postValue(validation)
    }

}