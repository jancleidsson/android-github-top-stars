package com.jss.githubtopstars

import androidx.paging.ExperimentalPagingApi
import com.jss.githubtopstars.core.repository.RepoRepositoryTest
import com.jss.githubtopstars.framework.repository.ReposDataSource
import com.jss.githubtopstars.framework.repository.ReposDataSourceTest
import com.jss.githubtopstars.framework.repository.ReposRemoteMediatorTest
import com.jss.githubtopstars.framework.vm.RepoListViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(Suite::class)
@Suite.SuiteClasses(ReposDataSourceTest::class, RepoRepositoryTest::class, ReposRemoteMediatorTest::class)
class UnitTestSuite