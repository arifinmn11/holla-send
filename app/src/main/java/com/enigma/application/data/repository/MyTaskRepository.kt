package com.enigma.application.data.repository

import com.enigma.application.data.model.courier_activity.get.ResponsePickUp
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.ResponseMyTasks


interface MyTaskRepository {
    suspend fun getMyTask(): ResponseMyTasks
    suspend fun postTaskToPickUp(): ResponsePickUp
    suspend fun putTaskToDone(id: String): ResponseMyTask
    suspend fun putTaskToUnAssign(id: String): ResponseMyTask
}