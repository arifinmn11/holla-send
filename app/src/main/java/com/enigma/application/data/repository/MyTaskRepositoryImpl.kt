package com.enigma.application.data.repository

import com.enigma.application.data.api.AuthApi
import com.enigma.application.data.api.MyTaskApi
import com.enigma.application.data.model.mytask.ResponseMyTask
import javax.inject.Inject

class MyTaskRepositoryImpl @Inject constructor(val myTaskApi: MyTaskApi) : MyTaskRepository {
    override suspend fun getMyTask(): ResponseMyTask = myTaskApi.getTaskUnFinished()
}