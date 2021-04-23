package com.jss.githubtopstars.framework.di

import android.app.Application
import io.mockk.mockkConstructor
import org.hamcrest.Matchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.mock

class ApplicationModuleTest {
    @Test
    fun providesApp_returnApplication() {
        val application = mock(Application::class.java)
        assertThat(ApplicationModule(application).providesApp(), `is`(application))
    }
}