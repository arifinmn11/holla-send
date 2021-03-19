package com.enigma.application.data.api.interceptor

import android.content.SharedPreferences
import com.enigma.application.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(val sharedPreferences: SharedPreferences) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(Constants.TOKEN, "")
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().header("Authorization", "Bearer $token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}