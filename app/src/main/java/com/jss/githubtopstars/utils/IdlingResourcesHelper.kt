package com.jss.githubtopstars.utils

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResourcesHelper {

    private const val RESOURCE_NAME = "GLOBAL"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE_NAME)

    fun incrementCounting() {
        countingIdlingResource.increment()
    }

    fun decrementCounting() {
        if(countingIdlingResource.isIdleNow.not()) {
            countingIdlingResource.decrement()
        }
    }
}