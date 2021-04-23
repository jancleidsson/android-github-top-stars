package com.jss.githubtopstars.core.data

import org.hamcrest.Matchers.isA
import org.junit.Assert.*
import org.junit.Test

class PageIndexTest {
    @Test
    fun validatesPageIndexScheme() {
        val pageIndex = PageIndex(1, 1, 3)
        assertThat(pageIndex.repoId, isA(Long::class.java))
        assertThat(pageIndex.prevPageIndex, isA(Int::class.java))
        assertThat(pageIndex.nextPageIndex, isA(Int::class.java))
    }
}