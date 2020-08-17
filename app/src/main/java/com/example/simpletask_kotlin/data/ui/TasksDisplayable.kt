package com.example.simpletask_kotlin.data.ui

sealed class TasksDisplayable
object InProgress : TasksDisplayable()
object NoData : TasksDisplayable()
data class Success(val tasks: List<TaskViewData>) : TasksDisplayable()
data class Error(val message: String?) : TasksDisplayable()
