package com.jss.githubtopstars.framework.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RepositoryDao {
    @Insert(onConflict = REPLACE)
    suspend fun addRepos(reposList: List<RepositoryEntity>)

    @Transaction
    @Query("SELECT * FROM 'repository'")
    fun getRepos(): DataSource.Factory<Int, RepositoryEntity>
}