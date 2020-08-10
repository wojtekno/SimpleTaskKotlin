package com.example.simpletask_kotlin.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single


interface TasksRepository {
    fun getAllTasks(): Observable<List<TaskData>>
    fun getTaskById(id: Int): Single<TaskData>
}