package com.jss.githubtopstars.core.usecase

import com.jss.githubtopstars.core.repository.RepoRepository

class GetAllRepos(private val repoRepository: RepoRepository) {
    operator fun invoke() = repoRepository.getAll()
}