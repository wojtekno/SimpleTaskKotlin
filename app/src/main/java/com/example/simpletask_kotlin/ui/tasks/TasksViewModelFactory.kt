package com.example.simpletask_kotlin.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.domain.ChangeTaskStateUseCase
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase

class TasksViewModelFactory(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val changeTaskStateUseCase: ChangeTaskStateUseCase,
    private val schedulerProvider: SchedulerProvider
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TasksViewModel(getAllTasksUseCase, changeTaskStateUseCase, schedulerProvider) as T
    }


}