package com.enigma.application.data.repository

import com.enigma.application.data.api.DashboardApi
import com.enigma.application.data.model.dashboard.ResponseDashboard
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(private val dashboardApi: DashboardApi) :
    DashboardRepository {

    override suspend fun getDashboard(): ResponseDashboard =
        dashboardApi.getApiDashboard()

}