package com.enigma.application.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.di.qualifier.PostAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(@PostAuth private val repository: AuthRepository) : ViewModel() {

    fun postAuth() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseAuth? = null
            try {
                val req = RequestAuth(email = "email@gmail.com", password = "admin1234")
                response = repository.postAuth(req)
            } catch (e: Exception) {
                response =
                    ResponseAuth(
                        status = 400,
                        message = "Error, try again",
                        data = null
                    )
                emit(response)
            } finally {
                Log.d("RESPONSE", "$response")
                emit(response)
            }

        }
    }

}