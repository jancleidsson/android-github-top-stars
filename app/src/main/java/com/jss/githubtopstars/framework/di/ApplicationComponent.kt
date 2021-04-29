package com.jss.githubtopstars.framework.di

import androidx.paging.ExperimentalPagingApi
import com.jss.githubtopstars.presentation.RepoListActivity

import dagger.Component
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
@Component(modules = [ApplicationModule::class, DatabaseServiceModule::class, GithubServiceModule::class, RepositoryModule::class, UsesCasesModule::class, RepoPagerModule::class])
interface ApplicationComponent {
    fun inject(repoListActivity: RepoListActivity)
}