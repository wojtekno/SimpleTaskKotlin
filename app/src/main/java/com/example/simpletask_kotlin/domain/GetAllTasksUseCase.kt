package com.example.simpletask_kotlin.domain

import com.example.simpletask_kotlin.data.ui.TaskViewData
import io.reactivex.rxjava3.core.Observable

interface GetAllTasksUseCase {
    fun getAllTasks(): Observable<List<TaskViewData>>
}