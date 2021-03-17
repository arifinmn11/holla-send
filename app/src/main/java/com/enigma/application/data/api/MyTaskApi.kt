package com.enigma.application.data.api

import com.enigma.application.data.model.mytask.ResponseMyTask
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MyTaskApi {
    @GET("task/my-task/unfinished")
    suspend fun getTaskUnFinished(): ResponseMyTask

    @POST("courier-activity")
    suspend fun postTaskToPickUp(): ResponseMyTask

    @PUT("task/finish/{id}")
    suspend fun putTaskToDone(
        @Path("id") id: String
    ): ResponseMyTask

    @PUT("/task/assign/cancel/{id}")
    suspend fun putTaskToCancel(
        @Path("id") id: String
    ): ResponseMyTask

}