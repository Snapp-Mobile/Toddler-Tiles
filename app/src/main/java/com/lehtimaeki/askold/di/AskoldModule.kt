package com.lehtimaeki.askold.di

import android.content.Context
import android.content.SharedPreferences
import com.lehtimaeki.askold.data.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AskoldModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(UserPreferences.ASKOLD_SHARED_PREFS, Context.MODE_PRIVATE)
    }
}