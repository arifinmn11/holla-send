package com.enigma.application.data.repository

import com.enigma.application.data.api.CourierActivityApi
import com.enigma.application.data.model.courier_activty.ResponseCourierActivity
import javax.inject.Inject

class CourierActivityRepositoryImpl @Inject constructor(private val courierActivityApi: CourierActivityApi) :
    CourierActivityRepository {
    override suspend fun getActivity(): ResponseCourierActivity =
        courierActivityApi.getApiCourierActive()

    override suspend fun putActivity(): ResponseCourierActivity =
        courierActivityApi.putApiCourierActive()

}