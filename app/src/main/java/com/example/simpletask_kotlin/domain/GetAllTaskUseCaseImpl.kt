package com.example.simpletask_kotlin.domain

import com.example.simpletask_kotlin.data.Status
import com.example.simpletask_kotlin.data.ui.TaskViewData
import io.reactivex.rxjava3.core.Observable

class GetAllTaskUseCaseImpl : GetAllTasksUseCase {
    override fun getAllTasks(): Observable<List<TaskViewData>> {
        return Observable.just(mockData())
//        return Observable.error(Throwable("some error"))
    }

    private fun mockData(): List<TaskViewData> {
        return listOf(
            TaskViewData(1, "task one", Status.OPEN),
            TaskViewData(2, "task two", Status.OPEN),
            TaskViewData(3, "task three", Status.OPEN),
            TaskViewData(4, "task four", Status.OPEN),
            TaskViewData(5, "task five", Status.OPEN)
        )
    }
}