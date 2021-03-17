package com.enigma.application.data.repository

import com.enigma.application.data.api.MyTaskApi
import com.enigma.application.data.model.mytask.ResponseMyTask
import com.enigma.application.data.model.mytask.ResponseMyTasks
import javax.inject.Inject

class MyTaskRepositoryImpl @Inject constructor(private val myTaskApi: MyTaskApi) :
    MyTaskRepository {
    override suspend fun getMyTask(): ResponseMyTasks =
        myTaskApi.getTaskUnFinished()

    override suspend fun postTaskToPickUp(): ResponseMyTasks =
        myTaskApi.postTaskToPickUp()

    override suspend fun putTaskToDone(id: String): ResponseMyTask =
        myTaskApi.putTaskToDone(id)

    override suspend fun putTaskToUnAssign(id: String): ResponseMyTask =
        myTaskApi.putTaskToCancel(id)
}