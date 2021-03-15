package com.enigma.application.di.module

import com.enigma.application.data.api.AuthApi
import com.enigma.application.data.api.NewTaskApi
import com.enigma.application.data.api.RegistrationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegistrationApi(retrofit: Retrofit): RegistrationApi {
        return retrofit.create(RegistrationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNewTaskApi(retrofit: Retrofit): NewTaskApi {
        return retrofit.create(NewTaskApi::class.java)
    }

}