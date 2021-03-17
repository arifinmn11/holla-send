package com.enigma.application.di.qualifier

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PostAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ValidationAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GetProfile

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PostRegistration

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GetNewTaskWaiting

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PutAddToMyTask


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GetMyTaskUnFinished


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CourierActivity