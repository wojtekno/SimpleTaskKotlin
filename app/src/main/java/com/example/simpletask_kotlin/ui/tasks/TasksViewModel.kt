package com.example.simpletask_kotlin.ui.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.data.ui.*
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class TasksViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val disposables = CompositeDisposable()
    val uiState: MutableLiveData<TasksDisplayable> = MutableLiveData(InProgress)

    init {
        val allTaskDisposable = getAllTasksUseCase.getAllTasks()
//            .delay(3000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = { lTasks ->
//                    Log.d("tag", "thred " + Thread.currentThread().name)
                    uiState.value = when {
                        lTasks.isEmpty() -> NoData
                        else -> Success(lTasks)
                    }
                },
                onError = {
//                    Log.d("tag", "thred " + Thread.currentThread().name)
                    uiState.value = Error(it.message)
                }
            )
        disposables.add(allTaskDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}