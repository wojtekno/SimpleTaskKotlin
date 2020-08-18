package com.example.simpletask_kotlin.ui.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.data.Status
import com.example.simpletask_kotlin.data.ui.*
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class TasksViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `given getAllTasksUseCase hasn't emitted value yet when initializing viewModel then state is InProgress`() {
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.never()
        })

        expectThat(viewModel.uiState.value).isEqualTo(InProgress)
    }

    @Test
    fun `given getAllTasksUseCase emitted error when initializing viewModel then state is Error`() {
        val mockThrowable = Throwable("some error")
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.error(mockThrowable)
        })

        expectThat(viewModel.uiState.value).isEqualTo(Error(mockThrowable.message))
    }

    @Test
    fun `given getAllTasksUseCase emitted empty list when initializing viewModel then state is NoData`() {
        val emptyList = emptyList<TaskViewData>()
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(emptyList)
        })

        expectThat(viewModel.uiState.value).isEqualTo(NoData)
    }

    @Test
    fun `given getAllTasksUseCase emitted data when initializing viewModel then state is Success`() {
        val mockTasks = listOf(
            TaskViewData(1, "first task", Status.OPEN)
        )
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(mockTasks)
        })

        expectThat(viewModel.uiState.value).isEqualTo(Success(mockTasks))
    }

    @Test
    fun `given getAllTasksUseCase emitted data multiple times when initializing viewModel then state is Success with the most recent data`() {
        val mockTasks = listOf(
            TaskViewData(1, "first task", Status.OPEN)
        )
        val mockTasks2 = listOf(
            TaskViewData(1, "first task", Status.OPEN),
            TaskViewData(2, "task 2", Status.OPEN),
            TaskViewData(3, "task 3", Status.OPEN)
        )
        val mockTasks3 = listOf(
            TaskViewData(1, "task 1", Status.OPEN),
            TaskViewData(4, "task 4", Status.OPEN)
        )
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.create { emitter ->
                emitter.onNext(mockTasks)
                emitter.onNext(mockTasks2)
                emitter.onNext(mockTasks3)
            }
        })

        expectThat(viewModel.uiState.value).isEqualTo(Success(mockTasks3))
    }


    @Test
    fun `given getAllTasksUseCase hasn't completed yet when viewModel onCleared invoked then dispose the observable`() {
        //todo implement test - how to test onCleared and disposable?
        assert(false)
    }

    private fun createViewModel(
        getAllTasksUseCase: GetAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.never()
        },
        schedulerProvider: SchedulerProvider = mock {
            on { io() } doReturn Schedulers.trampoline()
            on { ui() } doReturn Schedulers.trampoline()
        }
    ): TasksViewModel {
        return TasksViewModel(getAllTasksUseCase, schedulerProvider)
    }

}