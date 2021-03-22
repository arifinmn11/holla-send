package com.enigma.application.di.module

import com.enigma.application.data.repository.*
import com.enigma.application.di.qualifier.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepoModule {

    @Binds
    @PostAuth
    abstract fun bindRepositoryPostAuth(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @GetProfile
    abstract fun bindRepositoryGetProfile(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @PostRegistration
    abstract fun bindRepositoryPostRegistration(repositoryImpl: RegistrationRepositoryImpl): RegistrationRepository

    @Binds
    @GetNewTaskWaiting
    abstract fun bindRepositoryNewTask(repositoryIpl: NewTaskRepositoryImpl): NewTaskRepository

    @Binds
    @PutAddToMyTask
    abstract fun bindRepositoryAddToMyTask(repositoryImpl: NewTaskRepositoryImpl): NewTaskRepository

    @Binds
    @GetMyTaskUnFinished
    abstract fun bindRepositoryGetMyTaskUnFinished(repositoryImpl: MyTaskRepositoryImpl): MyTaskRepository

    @Binds
    @CourierActivity
    abstract fun bindRepositoryCheckCourierActivity(repositoryImpl: CourierActivityRepositoryImpl): CourierActivityRepository

    @Binds
    @Dashboard
    abstract fun bindRepositoryGetDashboard(repositoryImpl: DashboardRepositoryImpl): DashboardRepository

    @Binds
    @History
    abstract fun bindRepositoryGetHistory(repositoryImpl: HistoryRepositoryImpl): HistoryRepository
}

