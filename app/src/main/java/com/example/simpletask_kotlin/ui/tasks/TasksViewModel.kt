package com.example.simpletask_kotlin.ui.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.simpletask_kotlin.R
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.data.Status
import com.example.simpletask_kotlin.data.ui.*
import com.example.simpletask_kotlin.domain.ChangeTaskStateUseCase
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import com.example.simpletask_kotlin.utils.StatusChangeEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.jetbrains.annotations.TestOnly

class TasksViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val changeTaskStateUseCase: ChangeTaskStateUseCase,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val disposables = CompositeDisposable()
    val uiState: MutableLiveData<TasksUiState> = MutableLiveData(InProgress)
    private val _isAnyInProgress = MutableLiveData<Boolean>(false)
    val isAnyInProgress: LiveData<Boolean> get() = _isAnyInProgress
    private var _statusChangeMessage = MutableLiveData<StatusChangeEvent>()
        @TestOnly set

    init {
        val allTaskDisposable = getAllTasksUseCase.getAllTasks()
            .subscribeOn(schedulerProvider.io())
            .map {
                var lIsAnyInProgress = false
                for (task: TaskViewData in it) {
                    if (task.status != Status.AVAILABLE) {
                        lIsAnyInProgress = true
                        break
                    }
                }
                _isAnyInProgress.postValue(lIsAnyInProgress)
                return@map it
            }
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onNext = {
                    uiState.value = when {
                        it.isEmpty() -> NoData
                        else -> Success(it)
                    }
                },
                onError = {
                    uiState.value = Error(it.message)
                }
            )
        disposables.add(allTaskDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("tag", "onCleared")
        disposables.dispose()
    }

    fun onTaskClicked(listIndex: Int): Boolean {
        changeTaskStateUseCase.onTaskClicked(listIndex).subscribeBy(
            onSuccess = {
                _statusChangeMessage.value = when (it) {
                    Status.TRAVELLING -> StatusChangeEvent(R.string.status_change_to_travelling)
                    Status.WORKING -> StatusChangeEvent(R.string.status_change_to_working)
                    Status.AVAILABLE -> StatusChangeEvent(R.string.status_change_to_available)
                }
            },
            onError = {
                _statusChangeMessage.value =
                    StatusChangeEvent(R.string.cannot_work_on_tasks_simultaneously)
            })
        return true
    }

    fun statusChangeMessage(): LiveData<Int?> =
        _statusChangeMessage.map { it.getContentIfNotHandled() }

}