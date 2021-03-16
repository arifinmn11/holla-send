package com.enigma.application.data.repository

import com.enigma.application.data.api.NewTaskApi
import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.data.model.newtask.put.ResponseAddTaskToMyTask
import javax.inject.Inject

class NewTaskRepositoryImpl @Inject constructor(val newTaskApi: NewTaskApi) : NewTaskRepository {

    override suspend fun getAllTaskWaiting(): ResponseNewTaskWaiting =
        newTaskApi.getNewTask()

    override suspend fun addTaskToMyTask(id: String): ResponseAddTaskToMyTask =
        newTaskApi.addToMyTask(id)

}