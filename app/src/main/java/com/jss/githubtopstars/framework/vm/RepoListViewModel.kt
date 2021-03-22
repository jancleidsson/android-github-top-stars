package com.jss.githubtopstars.framework.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jss.githubtopstars.framework.UseCases
import com.jss.githubtopstars.framework.di.ApplicationModule
import com.jss.githubtopstars.framework.di.DaggerViewModelComponent
import javax.inject.Inject

class RepoListViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var useCases: UseCases
    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build()
            .inject(this)
    }
}