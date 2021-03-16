package com.enigma.application.data.repository

import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting
import com.enigma.application.data.model.newtask.put.ResponseAddTaskToMyTask

interface NewTaskRepository {
    suspend fun getAllTaskWaiting(): ResponseNewTaskWaiting
    suspend fun addTaskToMyTask(id: String): ResponseAddTaskToMyTask
}