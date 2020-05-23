package com.example.shows.di.modules

import android.app.Application
import android.content.Context
import com.example.shows.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @ApplicationScope
    fun provideContext(): Context = app.applicationContext
}
