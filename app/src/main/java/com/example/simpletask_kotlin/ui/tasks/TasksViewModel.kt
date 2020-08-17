package com.example.simpletask_kotlin.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.data.ui.*
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import io.reactivex.rxjava3.kotlin.subscribeBy

class TasksViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val uiState: MutableLiveData<TasksDisplayable> = MutableLiveData(InProgress)

    init {
        getAllTasksUseCase.getAllTasks()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = { lTasks ->
                    uiState.value = when {
                        lTasks.isEmpty() -> NoData
                        else -> Success(lTasks)
                    }
                },
                onError = {
                    uiState.value = Error(it.message)
                }
            )
    }
}