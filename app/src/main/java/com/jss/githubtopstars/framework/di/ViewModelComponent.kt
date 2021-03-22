package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.framework.vm.RepoListViewModel
import dagger.Component

@Component(modules = [ApplicationModule::class, UsesCasesModule::class])
interface ViewModelComponent {
    fun inject(repoListViewModel: RepoListViewModel)
}