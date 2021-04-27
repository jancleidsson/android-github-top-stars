package com.jss.githubtopstars.presentation

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.jss.githubtopstars.R
import com.jss.githubtopstars.core.data.Owner
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.utils.FakeGithubService
import com.jss.githubtopstars.utils.ServiceLocator
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class ReposListActivityTest() {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<ReposListActivity>()

    private val githubService = FakeGithubService()
    private lateinit var databaseService: DatabaseService

    @Before
    fun initMockedService() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        databaseService = Room.inMemoryDatabaseBuilder(context, DatabaseService::class.java).build()

        ServiceLocator.githubService = githubService
        ServiceLocator.databaseService = databaseService
    }

    @Test
    fun activeReposList_DisplayedLoadViewOnInitialData() {
        githubService.buildServiceResponse(listOf(), 5000)
        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun activeReposList_DisplayedLoadItemData() {
        val repo = Repo(1, "jss", 1, 1, Owner("jss", ""))
        githubService.buildServiceResponse(listOf(repo))
        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.repository_name)).check(matches(withText(repo.name)))
        onView(withId(R.id.repository_owner_name)).check(matches(withText(repo.owner.login)))
    }
}