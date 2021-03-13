package com.enigma.application.di.module

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.data.repository.AuthRepositoryImpl
import com.enigma.application.data.repository.RegistrationRepository
import com.enigma.application.data.repository.RegistrationRepositoryImpl
import com.enigma.application.di.qualifier.GetProfile
import com.enigma.application.di.qualifier.PostAuth
import com.enigma.application.di.qualifier.PostRegistration
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Interceptor

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

}

