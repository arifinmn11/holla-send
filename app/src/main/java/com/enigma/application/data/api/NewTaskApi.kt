package com.enigma.application.data.api

import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.data.model.newtask.put.ResponseAddTaskToMyTask
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NewTaskApi {
    @GET("task/waiting")
    suspend fun getNewTask(): ResponseNewTaskWaiting

    @PUT("task/assign/{id}")
    suspend fun addToMyTask(
        @Path("id") id: String
    ): ResponseAddTaskToMyTask
}