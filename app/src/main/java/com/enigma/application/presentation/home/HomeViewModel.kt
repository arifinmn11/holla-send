package com.enigma.application.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.enigma.application.data.api.DashboardApi
import com.enigma.application.data.model.auth.ResponseAuth
import com.enigma.application.data.model.dashboard.ResponseDashboard
import com.enigma.application.data.repository.DashboardRepository
import com.enigma.application.di.qualifier.Dashboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(@Dashboard val repository: DashboardRepository) :
    ViewModel() {

    fun getDashboard() = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        withTimeout(5000) {
            var response: ResponseDashboard? = null
            try {
                response = repository.getDashboard()
            } catch (e: Exception) {
                response =
                    ResponseDashboard(
                        code = 400,
                        data = null,
                        message = "Email or Password invalid!",
                    )
            } finally {
                emit(response)
            }
        }
    }

}