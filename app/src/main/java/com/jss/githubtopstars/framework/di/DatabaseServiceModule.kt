package com.jss.githubtopstars.framework.di

import android.app.Application
import com.jss.githubtopstars.framework.db.DatabaseService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseServiceModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application) = DatabaseService.getInstance(app)
}