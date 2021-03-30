package com.jss.githubtopstars.framework

import com.jss.core.usecase.AddAllRepositories
import com.jss.core.usecase.GetAllRepositories

data class UseCases(
    val getAllRepositories: GetAllRepositories,
    val addAllRepositories: AddAllRepositories
)