package com.eversonhs.to_dolist.datasource

import com.eversonhs.to_dolist.model.Task

object TaskDataSource {
    private val tasks = arrayListOf<Task>()

    fun getTasks() = tasks.toList()

    fun insertTask(task: Task) {
        if(task.id == 0)
            tasks.add(task.copy(id = tasks.size + 1))
        else {
            tasks.remove(task)
            tasks.add(task)
        }
    }

    fun findById(id: Int) = tasks.find {
        it.id == id
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }
}