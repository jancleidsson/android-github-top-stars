package com.jss.githubtopstars.core.data

import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class RepoTest {

    @Mock
    private val ownerMock = Mockito.mock(Owner::class.java)

    @Test
    fun validateRepoScheme() {
        val repo = Repo(1, "jan", 3, 2, ownerMock)
        assertThat(repo.id, Matchers.isA(Long::class.java))
        assertThat(repo.name, Matchers.isA(String::class.java))
        assertThat(repo.stars, Matchers.isA(Int::class.java))
        assertThat(repo.forks, Matchers.isA(Int::class.java))
        assertThat(repo.owner, Matchers.isA(Owner::class.java))
    }
}