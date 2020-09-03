package com.example.simpletask_kotlin.data.ui

sealed class TasksUiState
object InProgress : TasksUiState()
object NoData : TasksUiState()
data class Success(val tasks: List<TaskViewData>) : TasksUiState()
data class Error(val message: String?) : TasksUiState()
