package com.enigma.application.data.api

import com.enigma.application.data.model.dashboard.ResponseDashboard
import com.enigma.application.data.model.history.ResponseHistory
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoryApi {
    @GET("task/my-task/finished")
    suspend fun getApiDashboard(
        @Path("page") page: Int? = 0,
        @Path("size") size: Int? = 10
    ): ResponseHistory
}