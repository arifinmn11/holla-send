package com.enigma.application.data.repository

import com.enigma.application.data.api.AuthApi
import com.enigma.application.data.api.NewTaskApi
import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import javax.inject.Inject

class NewTaskRepositoryImpl @Inject constructor(val newTaskApi: NewTaskApi) : NewTaskRepository {

    override suspend fun getAllTaskWaiting(): ResponseNewTaskWaiting =
        newTaskApi.getNewTask()

}