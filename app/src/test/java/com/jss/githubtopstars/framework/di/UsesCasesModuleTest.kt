package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.core.repository.RepoRepository
import com.jss.githubtopstars.framework.UseCases
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.any
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock

class UsesCasesModuleTest {
    @Test
    fun getUseCases_returnsUseCases() {
        val repoRepository = mock(RepoRepository::class.java)
        assertThat(UsesCasesModule().getUseCases(repoRepository), `is`(any(UseCases::class.java)))
    }
}