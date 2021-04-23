package com.jss.githubtopstars.framework.di

import android.app.Application
import com.jss.githubtopstars.framework.db.DatabaseService
import io.mockk.every
import io.mockk.mockkObject
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock

class DatabaseServiceModuleTest {

    @Test
    fun provideDatabase_returnsDataBaseService() {
        val databaseService = mock(DatabaseService::class.java)
        val application = mock(Application::class.java)
        mockkObject(DatabaseService.Companion)
        every { DatabaseService.getInstance(application) } returns databaseService

        assertThat(DatabaseServiceModule().provideDatabase(application), `is`(databaseService))
    }
}