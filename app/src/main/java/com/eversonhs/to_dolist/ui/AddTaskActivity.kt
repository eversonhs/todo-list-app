package com.eversonhs.to_dolist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eversonhs.to_dolist.databinding.ActivityAddTaskBinding
import com.eversonhs.to_dolist.datasource.TaskDataSource
import com.eversonhs.to_dolist.extensions.format
import com.eversonhs.to_dolist.extensions.text
import com.eversonhs.to_dolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        insertListeners()
    }

    private fun insertListeners() {
        insertDateInputListener()
        insertTimeInputListener()
        insertCreateTaskButtonListener()
        insertCancelButtonListener()
    }

    private fun insertDateInputListener() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time)* -1
                binding.tilDate.text = Date(it+offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun insertTimeInputListener() {
        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"
            val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"
            timePicker.addOnPositiveButtonClickListener {
                binding.tilTime.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
    }

    private fun insertCreateTaskButtonListener() {
        binding.btnCreateTask.setOnClickListener {
            val task = Task (
                binding.tilTitle.text,
                binding.tilDescription.text,
                binding.tilTime.text,
                binding.tilDate.text
            )
            TaskDataSource.insertTask(task)

        }
    }

    private fun insertCancelButtonListener() {
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

}
