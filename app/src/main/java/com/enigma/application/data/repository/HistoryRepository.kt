package com.enigma.application.data.repository

import com.enigma.application.data.model.history.ResponseHistory

interface HistoryRepository {
    suspend fun getHistory(page: Int, size: Int): ResponseHistory
}