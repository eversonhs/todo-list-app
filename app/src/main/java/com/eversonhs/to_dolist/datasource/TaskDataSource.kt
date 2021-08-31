package com.eversonhs.to_dolist.datasource

import com.eversonhs.to_dolist.model.Task

object TaskDataSource {
    private val tasks = arrayListOf<Task>()

    fun getTasks() = tasks

    fun insertTask(task: Task) {
        tasks.add(task.copy(id = tasks.size+1))
    }


}