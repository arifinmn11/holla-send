package com.enigma.application.data.repository

import com.enigma.application.data.model.mytask.ResponseMyTask


interface MyTaskRepository {
    suspend fun getMyTask(): ResponseMyTask
    suspend fun postTaskToPickUp(): ResponseMyTask
    suspend fun putTaskToDone(id: String): ResponseMyTask
    suspend fun putTaskToUnAssign(id: String): ResponseMyTask
}