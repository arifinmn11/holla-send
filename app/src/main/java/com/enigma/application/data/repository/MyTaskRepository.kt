package com.enigma.application.data.repository

import com.enigma.application.data.model.mytask.ResponseMyTask


interface MyTaskRepository {
    suspend fun getMyTask(): ResponseMyTask
}