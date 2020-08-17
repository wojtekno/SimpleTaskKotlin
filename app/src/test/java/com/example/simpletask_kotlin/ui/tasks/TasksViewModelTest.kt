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
    fun `given repository hasn't emitted value yet when initializing viewModel then state is InProgress`() {
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.never()
        })

        expectThat(viewModel.uiState.value).isEqualTo(InProgress)
    }

    @Test
    fun `given repository emitted error when initializing viewModel then state is Error`() {
        val mockThrowable = Throwable("some error")
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.error(mockThrowable)
        })


        expectThat(viewModel.uiState.value).isEqualTo(Error(mockThrowable.message))
    }

    @Test
    fun `given repository emitted empty list when initializing viewModel then state is NoData`() {
        val emptyList = emptyList<TaskViewData>()
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(emptyList)
        })

        expectThat(viewModel.uiState.value).isEqualTo(NoData)
    }

    @Test
    fun `given repository emitted data when initializing viewModel then state is Success`() {
        val mockTasks = listOf(
            TaskViewData(1, "first task", Status.OPEN)
        )
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(mockTasks)
        })

        expectThat(viewModel.uiState.value).isEqualTo(Success(mockTasks))
    }


//    @Test
//    fun `given repository emits value when initializing viewModel then set tasks`() {
//
//        val mockTasks = listOf(
//            TaskViewData(
//                1,
//                "first task",
//                Status.OPEN
//            )
//        )
//        val viewModel = createViewModel(mock {
//            on { getAllTasks() } doReturn Observable.just(mockTasks)
//        })
//
//        expectThat(viewModel.tasks.value).isEqualTo(mockTasks)
//        expectThat(viewModel.errorMessage.value).isEqualTo(null)
//    }


//    @Test
//    fun `given repository emits error when initializing viewModel then show error`() {
//
//        val mockThrowable = Throwable("some error")
//        val viewModel = createViewModel(mock {
//            on { getAllTasks() } doReturn Observable.error(mockThrowable)
//        })
//
//        expectThat(viewModel.tasks.value).isEqualTo(null)
//        expectThat(viewModel.errorMessage.value).isEqualTo(mockThrowable.message)
//    }


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