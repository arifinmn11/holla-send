package com.enigma.application.data.api

import com.enigma.application.data.model.courier_activty.ResponseCourierActivity
import retrofit2.http.GET
import retrofit2.http.PUT

interface CourierActivityApi {

    @GET("courier-activity/active")
    suspend fun getApiCourierActive(): ResponseCourierActivity

    @PUT("courier-activity")
    suspend fun putApiCourierActive(): ResponseCourierActivity

}