package com.enigma.application.data.repository

import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.ResponseMyTasks


interface MyTaskRepository {
    suspend fun getMyTask(): ResponseMyTasks
    suspend fun postTaskToPickUp(): ResponseMyTasks
    suspend fun putTaskToDone(id: String): ResponseMyTask
    suspend fun putTaskToUnAssign(id: String): ResponseMyTask
}