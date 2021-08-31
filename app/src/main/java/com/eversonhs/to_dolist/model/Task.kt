package com.eversonhs.to_dolist.model

data class Task(
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val id: Int = 0
)
