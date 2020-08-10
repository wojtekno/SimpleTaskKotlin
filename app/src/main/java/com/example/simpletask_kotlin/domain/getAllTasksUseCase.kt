package com.example.simpletask_kotlin.domain

import com.example.simpletask_kotlin.data.TaskViewData
import io.reactivex.rxjava3.core.Observable

interface getAllTasksUseCase {
    fun getAllTasks(): Observable<List<TaskViewData>>
}