package com.jss.githubtopstars.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_index")
class PageIndex(
        @PrimaryKey val repoId: Long,
        val prevPageIndex: Int?,
        val nextPageIndex: Int?
)