package com.jss.githubtopstars.framework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jss.githubtopstars.core.data.PageIndex

@Dao
interface PageIndexDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPageIndexes(remoteKey: List<PageIndex>)

    @Query("SELECT * FROM page_index WHERE repoId = :repoId")
    suspend fun pageIndexByRepoId(repoId: Long): PageIndex?

    @Query("DELETE FROM page_index")
    suspend fun deleteAllPageIndexes()
}