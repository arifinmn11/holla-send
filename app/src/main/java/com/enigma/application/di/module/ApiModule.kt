package com.enigma.application.di.module

import com.enigma.application.data.api.*
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

    @Singleton
    @Provides
    fun provideMyTaskApi(retrofit: Retrofit): MyTaskApi {
        return retrofit.create(MyTaskApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCourierActivity(retrofit: Retrofit): CourierActivityApi {
        return retrofit.create(CourierActivityApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDashboardApi(retrofit: Retrofit): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHistoryApi(retrofit: Retrofit): HistoryApi {
        return retrofit.create(HistoryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfile(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

}