package com.jss.githubtopstars.framework.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jss.core.data.Repository
import com.jss.githubtopstars.framework.UseCases
import com.jss.githubtopstars.framework.di.ApplicationModule
import com.jss.githubtopstars.framework.di.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var useCases: UseCases

    init {
        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build()
            .inject(this)
    }

    val reposList = MutableLiveData<List<Repository>>()
    val loading = MutableLiveData<Boolean>()
    var jobGetRepos: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        jobGetRepos?.cancel()
    }

    fun getRepositories() {
        loading.postValue(true)
        jobGetRepos = coroutineScope.launch {
            val repoList = useCases.getAllRepos()
            loading.postValue(false)
            reposList.postValue(repoList)
        }
    }
}