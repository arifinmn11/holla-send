package com.enigma.application.data.api

import com.enigma.application.utils.Constans
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constans.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
}