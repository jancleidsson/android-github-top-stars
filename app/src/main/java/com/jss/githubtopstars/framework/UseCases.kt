package com.jss.githubtopstars.framework

import com.jss.githubtopstars.core.usecase.GetAllRepositories

data class UseCases(
        val getAllRepos: GetAllRepositories,
)