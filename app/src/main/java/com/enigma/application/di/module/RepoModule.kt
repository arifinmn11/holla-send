package com.enigma.application.di.module

import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.data.repository.AuthRepositoryImpl
import com.enigma.application.di.qualifier.PostAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepoModule {

    @PostAuth
    @Binds
    abstract fun bindRepositoryPostAuth(repositoryImpl: AuthRepositoryImpl): AuthRepository
}

