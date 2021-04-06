package com.jss.githubtopstars.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jss.githubtopstars.core.data.PageIndex
import com.jss.githubtopstars.core.data.Repo

@Database(entities = [Repo::class, PageIndex::class], version = 1, exportSchema = false)
abstract class DatabaseService : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "repository.db"

        private var instance: DatabaseService? = null

        private fun create(context: Context): DatabaseService =
                Room.databaseBuilder(context, DatabaseService::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()

        fun getInstance(context: Context): DatabaseService =
                instance ?: synchronized(this) {
                    instance ?: create(context).also { instance = it }
                }
    }

    abstract fun repositoryDao(): RepoDao

    abstract fun pageIndexDao(): PageIndexDao
}