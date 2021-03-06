package com.jss.githubtopstars

import androidx.paging.ExperimentalPagingApi
import com.jss.githubtopstars.core.repository.RepoRepositoryTest
import com.jss.githubtopstars.framework.repository.RepoPagingDataSourceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs repository unit tests
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(Suite::class)
@Suite.SuiteClasses(RepoPagingDataSourceTest::class, RepoRepositoryTest::class, RepoRepositoryTest::class)
class UnitTestSuite