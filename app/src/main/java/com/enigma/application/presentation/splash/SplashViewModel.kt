package com.enigma.application.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.profile.ResponseProfile
import com.enigma.application.data.model.radius.ResponseRadius
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.di.qualifier.GetProfile
import com.enigma.application.di.qualifier.PostAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(@GetProfile val repository: AuthRepository) :
    ViewModel() {

    fun checkToken() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseProfile? = null
            try {
                response = repository.getMe()
            } catch (e: Exception) {
                response =
                    ResponseProfile(
                        code = 400,
                        message = "Error, try again",
                        data = null
                    )
            } finally {
                Log.d("RESPONSE", "$response")
                emit(response)
            }

        }
    }

    fun getRadius() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseRadius? = null
            try {
                response = repository.getRadius()
            } catch (e: Exception) {
                response =
                    ResponseRadius(
                        code = 400,
                        message = "Error, try again",
                        data = null
                    )
            } finally {
                emit(response)
            }

        }
    }

}