package com.example.simpletask_kotlin.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpletask_kotlin.data.TaskViewData
import com.example.simpletask_kotlin.domain.getAllTasksUseCase
import io.reactivex.rxjava3.kotlin.subscribeBy

class TasksViewModel(getAllTasksUseCase: getAllTasksUseCase) : ViewModel() {

    val tasks: MutableLiveData<List<TaskViewData>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        getAllTasksUseCase.getAllTasks()
            .subscribeBy(
                onNext = { lTasks ->
                    tasks.value = lTasks
                },
                onError = {
                    errorMessage.value = it.message
                }
            )
    }
}