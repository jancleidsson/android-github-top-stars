package com.jss.githubtopstars.framework.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jss.core.data.Owner
import com.jss.core.data.Repository

@Entity(tableName = "repository")
data class RepositoryEntity(
    val name: String,
    val stars: Int,
    val forks: Int,
    @Embedded
    val owner: Owner?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "repository_id")
    val repositoryId: Long = 0

) {
    companion object {
        fun fromRepository(repository: Repository) = RepositoryEntity(
            repository.name,
            repository.stars,
            repository.forks,
            repository.owner,
            repository.repositoryId
        )
    }
}