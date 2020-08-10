package com.example.simpletask_kotlin.ui.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.simpletask_kotlin.data.Status
import com.example.simpletask_kotlin.data.TaskViewData
import com.example.simpletask_kotlin.domain.getAllTasksUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class TasksViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Test
    fun `given repository emits value when initializing viewModel then set tasks`() {

        val mockTasks = listOf(TaskViewData(1, "first task", Status.OPEN))
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(mockTasks)
        })

        expectThat(viewModel.tasks.value).isEqualTo(mockTasks)
        expectThat(viewModel.errorMessage.value).isEqualTo(null)
    }


    @Test
    fun `given repository emits error when initializing viewModel then show error`() {

        val mockThrowable = Throwable("some error")
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.error(mockThrowable)
        })

        expectThat(viewModel.tasks.value).isEqualTo(null)
        expectThat(viewModel.errorMessage.value).isEqualTo(mockThrowable.message)
    }


    private fun createViewModel(
        getAllTasksUseCase: getAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.never()
        }
    ): TasksViewModel {
        return TasksViewModel(getAllTasksUseCase)
    }
}