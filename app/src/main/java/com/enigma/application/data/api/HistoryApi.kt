package com.enigma.application.data.api

import com.enigma.application.data.model.dashboard.ResponseDashboard
import com.enigma.application.data.model.history.ResponseHistory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApi {
    @GET("task/my-task/finished")
    suspend fun getApiDashboard(
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 10
    ): ResponseHistory
}