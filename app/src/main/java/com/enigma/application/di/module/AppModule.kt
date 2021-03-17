package com.enigma.application.di.module

import android.content.Context
import android.content.SharedPreferences
import com.enigma.application.data.api.interceptor.AuthTokenInterceptor
import com.enigma.application.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideOkHttp3(authTokenInterceptor: AuthTokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authTokenInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient, gson: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(gson)
            .build()
    }

    @Singleton
    @Provides
    fun provideSharePref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("SharedPreference", Context.MODE_PRIVATE)
    }

}