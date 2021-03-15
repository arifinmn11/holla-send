package com.enigma.application.data.api

import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface NewTaskApi {
    @GET("task/waiting")
    suspend fun getNewTask(): ResponseNewTaskWaiting
}