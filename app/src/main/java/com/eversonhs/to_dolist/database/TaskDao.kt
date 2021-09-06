package com.eversonhs.to_dolist.database

import androidx.room.*
import com.eversonhs.to_dolist.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id = (:taskId)")
    suspend fun findById(taskId: Int): Task?

    @Update
    suspend fun updateTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun removeTask(task: Task)
}
