package com.example.simpletask_kotlin.di

import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.domain.GetAllTaskUseCaseImpl
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import com.example.simpletask_kotlin.ui.tasks.TasksViewModelFactory
import com.example.simpletask_kotlin.utils.SchedulerProviderImpl

class AppGraph {

    private val schedulerProvider: SchedulerProvider = SchedulerProviderImpl()
    private val getAllTasksUseCase: GetAllTasksUseCase = GetAllTaskUseCaseImpl()
    val tasksViewModelFactory = TasksViewModelFactory(getAllTasksUseCase, schedulerProvider)


}