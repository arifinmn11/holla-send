package com.enigma.application.data.repository

import com.enigma.application.data.model.courier_activty.ResponseCourierActivity

interface CourierActivityRepository {
    suspend fun getActivity(): ResponseCourierActivity
    suspend fun putActivity(): ResponseCourierActivity
}