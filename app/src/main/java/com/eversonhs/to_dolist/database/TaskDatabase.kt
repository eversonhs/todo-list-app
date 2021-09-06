package com.eversonhs.to_dolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eversonhs.to_dolist.model.Task

@Database(entities = arrayOf(Task::class), version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun TaskDao(): TaskDao

    companion object {
        private const val DB_NAME = "task_database.db"
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
    }
}