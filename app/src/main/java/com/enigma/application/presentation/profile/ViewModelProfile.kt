package com.enigma.application.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.di.qualifier.GetProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ViewModelProfile @Inject constructor(@GetProfile val repository: AuthRepository) :
    ViewModel() {

    fun profileApi() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
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
}