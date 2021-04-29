package com.jss.githubtopstars.presentation

import android.content.Context
import android.os.PowerManager
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
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
import com.jss.githubtopstars.utils.ServiceLocator
import org.hamcrest.Description
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

        //Avoid screen off before test execution
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isInteractive.not()) {
            onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_POWER))
        }
    }

    @Test
    fun activeReposList_progressDisplayed_onAppLaunch() {
        githubService.buildServiceResponse(listOf(), 5000)

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.loading)).check(matches(isDisplayed()))
    }

    @Test
    fun activeReposList_listItemDisplayed_onLoadData() {
        val repo = Repo(1, "jss", 1, 1, Owner("jss", ""))
        val startsText = context.getString(R.string.starts).plus(Constants.EMPTY_SPACE).plus(repo.stars)
        val forksText = context.getString(R.string.forks).plus(Constants.EMPTY_SPACE).plus(repo.forks)
        githubService.buildServiceResponse(listOf(repo))

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.repository_name)).check(matches(withText(repo.name)))
        onView(withId(R.id.repository_stargazers_count)).check(matches(withText(startsText)))
        onView(withId(R.id.repository_forks_count)).check(matches(withText(forksText)))
        onView(withId(R.id.repository_owner_name)).check(matches(withText(repo.owner.login)))
        onView(withId(R.id.repository_owner_photo)).check(matches(hasDrawable()))
    }

    @Test
    fun activeReposList_DisplayedLoadReposError_onAppLaunch() {
        githubService.buildServiceResponse(throwsHTTPException = true)

        activityScenarioRule.scenario.recreate()
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
    }

    private fun hasDrawable(): BoundedMatcher<View, ImageView> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("has drawable content")
            }

            override fun matchesSafely(item: ImageView?): Boolean {
                return item?.drawable != null
            }
        }
    }
}