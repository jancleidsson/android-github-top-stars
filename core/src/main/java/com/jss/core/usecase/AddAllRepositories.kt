package com.jss.core.usecase

import com.jss.core.data.Repository
import com.jss.core.repository.RepoRepository

class AddAllRepositories(private val repoRepository: RepoRepository) {
    suspend operator fun invoke(reposList: List<Repository>) = repoRepository.addAll(reposList)
}