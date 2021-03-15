package com.enigma.application.data.repository

import com.enigma.application.data.model.newtask.ResponseNewTaskWaiting

interface NewTaskRepository {
    suspend fun getAllTaskWaiting(): ResponseNewTaskWaiting
}