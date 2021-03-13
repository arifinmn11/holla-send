package com.enigma.application.data.api.interceptor

import android.content.SharedPreferences
import android.util.Log
import com.enigma.application.utils.Constans
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(val sharedPreferences: SharedPreferences) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(Constans.TOKEN, "GET TOKEN")
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().header("Authorization", "Bearer $token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}