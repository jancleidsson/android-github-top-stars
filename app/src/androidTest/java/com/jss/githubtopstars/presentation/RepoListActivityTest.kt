package com.jss.githubtopstars.presentation

import android.content.Context
import android.os.PowerManager
import android.view.KeyEvent
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.jss.githubtopstars.R
import com.jss.githubtopstars.core.data.Owner
import com.jss.githubtopstars.core.data.Repo
import com.jss.githubtopstars.framework.db.DatabaseService
import com.jss.githubtopstars.utils.Constants
import com.jss.githubtopstars.utils.FakeGithubService
import com.jss.githubtopstars.utils.IdlingResourcesHelper
import com.jss.githubtopstars.utils.ServiceLocator
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class RepoListActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<RepoListActivity>()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val githubService = FakeGithubService()

    private lateinit var databaseService: DatabaseService

    @Before
    fun initMockedService() {
        databaseService = Room.inMemoryDatabaseBuilder(context, DatabaseService::class.java).build()

        ServiceLocator.githubService = githubService
        ServiceLocator.databaseService = databaseService
    }

    @Before
    fun makeDeviceInteractive() {
        //Avoid screen off before test execution
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isInteractive.not()) {
            onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_POWER))
        }
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(IdlingResourcesHelper.countingIdlingResource)
    }

    @After
    fun unRegisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(IdlingResourcesHelper.countingIdlingResource)
    }

    @Test
    fun activeReposList_progressDisplayed_onAppLaunch() {
        githubService.configServiceResponse(delay= 1000L)
        unRegisterIdlingResource()

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun activeReposList_retryDisplayed_onAppLaunchError() {
        githubService.configServiceResponse(delay=1000L, httpExceptionOnPage = 1)
        unRegisterIdlingResource()

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }

    @Test
    fun activeReposList_listItemDisplayed_onLoadDataAfterFirstPage() {
        val reversedCount = Int.MAX_VALUE - 51
        val repo = Repo(51, "jss 51", reversedCount, reversedCount, Owner("jss 51", ""))
        val starts = context.getString(R.string.starts).plus(Constants.EMPTY_SPACE).plus(repo.stars)
        val forks = context.getString(R.string.forks).plus(Constants.EMPTY_SPACE).plus(repo.forks)

        //Use drawable test
        githubService.configServiceResponse(delay= 1000L, responsePages = 2)

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.loading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.repo_list_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.repo_list_recycler_view)).perform(
                RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withText(repo.name)))
        )
        onView(withId(R.id.repo_list_recycler_view)).perform(
                RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withText(starts)))
        )
        onView(withId(R.id.repo_list_recycler_view)).perform(
                RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withText(forks)))
        )
        onView(withId(R.id.repo_list_recycler_view)).perform(
                RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withText(repo.owner.login)))
        )
        onView(withId(R.id.repo_list_recycler_view)).perform(
                RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withId(R.id.footer_item_progress)))
        )
    }

    @Test
    fun activeReposList_displaysListItemError_onLoadNewPage() {
        githubService.configServiceResponse(delay= 1000L, httpExceptionOnPage = 2)

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.repo_list_recycler_view)).perform(
            RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withId(R.id.footer_item_retry_button)))
        )

        onView(withId(R.id.repo_list_recycler_view)).perform(
            RecyclerViewActions.scrollTo<RepoPagingDataAdapter.RepoViewHolder>(hasDescendant(withText(containsString(context.getString(R.string.fetch_data_error)))))
        )
    }
}