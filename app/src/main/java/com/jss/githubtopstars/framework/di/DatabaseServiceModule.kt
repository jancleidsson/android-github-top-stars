package com.jss.githubtopstars.framework.di

import android.app.Application
import com.jss.githubtopstars.utils.ServiceLocator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseServiceModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application) = ServiceLocator.provideDatabaseService(app)
}