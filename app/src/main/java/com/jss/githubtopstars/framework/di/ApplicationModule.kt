package com.jss.githubtopstars.framework.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(val app: Application) {
    @Provides
    fun providesApp() = app
}