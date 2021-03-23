package com.jss.githubtopstars.framework.di

import com.jss.core.repository.RepoRepository
import com.jss.core.usecase.*
import com.jss.githubtopstars.framework.UseCases
import dagger.Module
import dagger.Provides

@Module
class UsesCasesModule {

    @Provides
    fun getUseCases(repository: RepoRepository) = UseCases(
        GetAllRepositories(repository)
    )
}