package com.jss.core.usecase

import com.jss.core.repository.RepoRepository

class GetAllRepositories(private val repoRepository: RepoRepository) {
    suspend operator fun invoke() = repoRepository.getAllRepositories()
}