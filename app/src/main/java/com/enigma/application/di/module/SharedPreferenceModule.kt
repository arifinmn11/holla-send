package com.enigma.application.di.module

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.Nonnull
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SharedPreferenceModule {

    private var preferences: SharedPreferences? = null

    @Inject
    fun PreferencesHelper(sharedPreferences: SharedPreferences?) {
        preferences = sharedPreferences
    }

    fun putString(@Nonnull key: String?, @Nonnull value: String?) {
        preferences!!.edit().putString(key, value).apply()
    }

    fun getString(@Nonnull key: String?): String? {
        return preferences!!.getString(key, "")
    }

    fun putBoolean(@Nonnull key: String?, @Nonnull value: Boolean) {
        preferences!!.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(@Nonnull key: String?): Boolean {
        return preferences!!.getBoolean(key, false)
    }

    fun putInt(@Nonnull key: String?, @Nonnull value: Boolean) {
        preferences!!.edit().putBoolean(key, value).apply()
    }

    fun getInt(@Nonnull key: String?): Int {
        return preferences!!.getInt(key, -1)
    }

    fun clear() {
        preferences!!.edit().clear().apply()
    }
}