package com.enigma.application.data.api

import com.enigma.application.data.model.dashboard.ResponseDashboard
import retrofit2.http.GET

interface DashboardApi {
    @GET("task/count")
    suspend fun getApiDashboard(): ResponseDashboard
}