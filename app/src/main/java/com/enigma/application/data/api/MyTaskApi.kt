package com.enigma.application.data.api

import com.enigma.application.data.model.mytask.ResponseMyTask
import retrofit2.http.GET
import retrofit2.http.POST

interface MyTaskApi {
    @GET("task/my-task/unfinished")
    suspend fun getTaskUnFinished(): ResponseMyTask

    @POST("courier-activity")
    suspend fun putTaskToPickUp(): ResponseMyTask
}