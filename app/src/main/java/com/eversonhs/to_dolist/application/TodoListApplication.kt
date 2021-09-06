package com.eversonhs.to_dolist.application

import android.app.Application
import com.eversonhs.to_dolist.database.TaskDatabase

class TodoListApplication: Application() {
    val database by lazy { TaskDatabase.getInstance(this) }
}