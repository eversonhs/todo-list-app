package com.eversonhs.to_dolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eversonhs.to_dolist.application.TodoListApplication
import com.eversonhs.to_dolist.database.TaskDatabase
import com.eversonhs.to_dolist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (application as TodoListApplication).database
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
        binding.rvTasks.adapter = adapter
        CoroutineScope(Dispatchers.Main).launch {
            updateList()
        }
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
            CoroutineScope(Dispatchers.Main).launch {
                database.TaskDao().removeTask(it)
                updateList()
            }
        }
        adapter.editListener = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.Main).launch {
                updateList()
            }
        }
    }

    private suspend fun updateList() {
        val tasks = database.TaskDao().getTasks()
        binding.emptyView.emptyState.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE

        adapter.submitList(tasks)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}