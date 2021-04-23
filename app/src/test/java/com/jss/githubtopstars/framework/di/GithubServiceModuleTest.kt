package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.framework.api.GithubService
import io.mockk.every
import io.mockk.mockkObject
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock

class GithubServiceModuleTest {

    @Test
    fun provideGithubService_returnsGitHubService() {
        val githubService = mock(GithubService::class.java)
        mockkObject(GithubService.Companion)
        every { GithubService.create() } returns githubService
        assertThat(GithubServiceModule().provideGithubService(), `is`(githubService))
    }
}