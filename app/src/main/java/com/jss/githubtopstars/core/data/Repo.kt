package com.jss.githubtopstars.core.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repository")
data class Repo(
        @PrimaryKey
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("stargazers_count")
        val stars: Int,
        @SerializedName("forks_count")
        val forks: Int,
        @Embedded
        val owner: Owner
)