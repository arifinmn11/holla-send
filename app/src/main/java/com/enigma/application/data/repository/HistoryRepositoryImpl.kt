package com.enigma.application.data.repository

import com.enigma.application.data.api.HistoryApi
import com.enigma.application.data.model.history.ResponseHistory
import javax.inject.Inject


class HistoryRepositoryImpl @Inject constructor(val historyApi: HistoryApi) : HistoryRepository {
    override suspend fun getHistory(page: Int, size: Int): ResponseHistory =
        historyApi.getApiDashboard(page, size)
}