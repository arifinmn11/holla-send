package com.enigma.application.di.module

import com.enigma.application.data.model.auth.RequestAuth
import com.enigma.application.data.repository.AuthRepository
import com.enigma.application.data.repository.AuthRepositoryImpl
import com.enigma.application.di.qualifier.PostAuth
import com.enigma.application.di.qualifier.ValidationAuth
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

}

