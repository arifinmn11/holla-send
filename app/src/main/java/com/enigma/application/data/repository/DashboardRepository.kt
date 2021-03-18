package com.enigma.application.data.repository

import com.enigma.application.data.model.dashboard.ResponseDashboard


interface DashboardRepository {
    suspend fun getDashboard(): ResponseDashboard
}