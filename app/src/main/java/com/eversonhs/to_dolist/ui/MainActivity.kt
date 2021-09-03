package com.eversonhs.to_dolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eversonhs.to_dolist.databinding.ActivityMainBinding
import com.eversonhs.to_dolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
        binding.rvTasks.adapter = adapter
        updateList()
    }

    private fun insertListeners() {
        insertAddTaskButtonListeners()
        insertAdapterListeners()
    }

    private fun insertAddTaskButtonListeners() {
        binding.btnAdd.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }
    }

    private fun insertAdapterListeners() {
        adapter.deleteListener = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
        adapter.editListener = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
            updateList()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()
        }
    }

    private fun updateList() {
        val tasks = TaskDataSource.getTasks()
        binding.emptyView.emptyState.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE

        adapter.submitList(tasks)
    }
    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}