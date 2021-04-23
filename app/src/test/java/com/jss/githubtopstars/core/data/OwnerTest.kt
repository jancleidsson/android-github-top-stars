package com.jss.githubtopstars.core.data

import org.hamcrest.Matchers.isA
import org.junit.Assert.*
import org.junit.Test

class OwnerTest {

    @Test
    fun validatesOwnerScheme() {
        val owner = Owner("jan", "photo")
        assertThat(owner.login, isA(String::class.java))
        assertThat(owner.photo, isA(String::class.java))
    }
}