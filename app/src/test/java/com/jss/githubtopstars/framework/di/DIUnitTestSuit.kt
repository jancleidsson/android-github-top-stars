package com.jss.githubtopstars.framework.di

import com.jss.githubtopstars.core.repository.RepoRepositoryTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs dependency injection unit tests
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(ApplicationModuleTest::class, DatabaseServiceModuleTest::class,
        GithubServiceModuleTest::class, RepoRepositoryTest::class, RepoPagerModuleTest::class, UsesCasesModuleTest::class)
class DIUnitTestSuit