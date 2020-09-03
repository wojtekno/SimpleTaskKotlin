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
        return generateTasks(Status.TRAVELLING, 2, 1000000)

    }

    private fun generateSmallData(): List<TaskViewData> {
        return listOf(
            TaskViewData(1, "task one", Status.AVAILABLE),
            TaskViewData(2, "task two", Status.AVAILABLE),
            TaskViewData(3, "task three", Status.AVAILABLE),
            TaskViewData(4, "task four", Status.AVAILABLE),
            TaskViewData(5, "task five", Status.AVAILABLE)
        )
    }

    private fun generateTasks(
        status: Status = Status.AVAILABLE,
        index: Int = 0,
        size: Int = 30
    ): List<TaskViewData> {
        val tasks = mutableListOf<TaskViewData>()
        for (i in 0 until size) {
            val task = if (index == i) TaskViewData(i, "task ${i + 1}", status)
            else TaskViewData(i, "task ${i + 1}", Status.AVAILABLE)
            tasks.add(task)
        }
        return tasks
    }
}