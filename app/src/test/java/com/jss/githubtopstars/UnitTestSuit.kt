package com.jss.githubtopstars

import androidx.paging.ExperimentalPagingApi
import com.jss.githubtopstars.framework.vm.RepoListViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests
@ExperimentalPagingApi
@RunWith(Suite::class)
@Suite.SuiteClasses(RepoListViewModelTest::class)
class UnitTestSuite