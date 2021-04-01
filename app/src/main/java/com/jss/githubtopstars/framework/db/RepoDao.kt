package com.jss.githubtopstars.framework.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.jss.githubtopstars.core.data.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = REPLACE)
    suspend fun addRepos(reposList: List<Repo>)

    @Transaction
    @Query("SELECT * FROM 'repository'")
    fun getRepos(): PagingSource<Int, Repo>
}