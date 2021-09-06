package com.eversonhs.to_dolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eversonhs.to_dolist.application.TodoListApplication
import com.eversonhs.to_dolist.database.TaskDatabase
import com.eversonhs.to_dolist.databinding.ActivityAddTaskBinding
import com.eversonhs.to_dolist.extensions.format
import com.eversonhs.to_dolist.extensions.text
import com.eversonhs.to_dolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddTaskActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var database: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (application as TodoListApplication).database
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        insertListeners()

        if(intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            CoroutineScope(Dispatchers.Main).launch {
                database.TaskDao().findById(taskId)?.let {
                    binding.tilTitle.text = it.title
                    binding.tilDescription.text = it.description
                    binding.tilDate.text = it.date
                    binding.tilTime.text = it.time
                }
            }
        }
    }

    private fun insertListeners() {
        insertToolbarNavigationListener()
        insertDateInputListeners()
        insertTimeInputListeners()
        insertCreateTaskButtonListeners()
        insertCancelButtonListeners()
    }

    private fun insertToolbarNavigationListener() {
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun insertDateInputListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time)*-1
                binding.tilDate.text = Date(it+offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun insertTimeInputListeners() {
        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"
                binding.tilTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
    }

    private fun insertCreateTaskButtonListeners() {
        binding.btnCreateTask.setOnClickListener {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            val task = Task (
                binding.tilTitle.text,
                binding.tilDescription.text,
                binding.tilTime.text,
                binding.tilDate.text,
                taskId
            )
            if(taskId == 0)
                CoroutineScope(Dispatchers.Main).launch {
                    database.TaskDao().insertTask(task)
                }
            else
                CoroutineScope(Dispatchers.Main).launch {
                    database.TaskDao().updateTask(task)
                }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun insertCancelButtonListeners() {
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "TASK_ID"
    }

}
