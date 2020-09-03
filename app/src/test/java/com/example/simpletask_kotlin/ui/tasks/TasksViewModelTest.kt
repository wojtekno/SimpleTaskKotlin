package com.example.simpletask_kotlin.ui.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.simpletask_kotlin.R
import com.example.simpletask_kotlin.SchedulerProvider
import com.example.simpletask_kotlin.data.Status
import com.example.simpletask_kotlin.data.ui.*
import com.example.simpletask_kotlin.domain.ChangeTaskStateUseCase
import com.example.simpletask_kotlin.domain.GetAllTasksUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
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
        val mockTasks = getAvailableTasks(1).toList()
        val viewModel = createViewModel(mock {
            on { getAllTasks() } doReturn Observable.just(mockTasks)
        })

        expectThat(viewModel.uiState.value).isEqualTo(Success(mockTasks))
    }

    @Test
    fun `given getAllTasksUseCase emitted data multiple times when initializing viewModel then state is Success with the most recent data`() {
        val mockTasks = getAvailableTasks(1)
        val mockTasks2 = getAvailableTasks(4)
        val mockTasks3 = getAvailableTasks(2)
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
        assert(true)
    }

    @Test
    fun `given getAllTasksUseCase emitted all tasks with Available state when initializing viewModel then isAnyInProgress is false`() {
        val tasks = getAvailableTasks(10).toList()
        val vm = createViewModel(getAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.just(tasks)
        })
        expectThat(vm.isAnyInProgress.value).isEqualTo(false)
    }

    @Test
    fun `given getAllTasksUseCase emitted tasks with one having Travelling state when initializing viewModel then isAnyInProgress is true`() {
        val tasks = getTasksWithOneHavingDifferentStatus(Status.TRAVELLING, 1).toList()
        val vm = createViewModel(getAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.just(tasks)
        })
        expectThat(vm.isAnyInProgress.value).isEqualTo(true)
    }

    @Test
    fun `given getAllTasksUseCase emitted tasks with one having Working state when initializing viewModel then isAnyInProgress is true`() {
        val tasks = getTasksWithOneHavingDifferentStatus(Status.WORKING, 1).toList()
        val vm = createViewModel(getAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.just(tasks)
        })
        expectThat(vm.isAnyInProgress.value).isEqualTo(true)
    }

    @Test
    fun `given uiState is Success when onTaskClicked then changeTaskStateUseCase-onTaskClicked invoked`() {
        val mockChangeTaskStateUseCase: ChangeTaskStateUseCase =
            mock { on { onTaskClicked(3) } doReturn Single.never() }
        val vm = createViewModel(changeTaskStateUseCase = mockChangeTaskStateUseCase).apply {
            uiState.value = Success(getAvailableTasks())
        }

        vm.onTaskClicked(3)
        verify(mockChangeTaskStateUseCase).onTaskClicked(3)
    }

    @Test
    fun `given uiState is Success and changeTaskStateUseCase returns error when onTaskClicked then statusChangeMessage is cannot_work_on_tasks_simultaneously`() {
        val vm = createViewModel(changeTaskStateUseCase = mock {
            on { onTaskClicked(3) } doReturn Single.error(Throwable("Operation not supported"))
        }).apply {
            uiState.value = Success(getAvailableTasks())
        }
        val statusChangeMessage = vm.statusChangeMessage()
        statusChangeMessage.observeForever(mock())
        vm.onTaskClicked(3)
        expectThat(statusChangeMessage.value).isEqualTo(R.string.cannot_work_on_tasks_simultaneously)
    }

    @Test
    fun `given uiState is Success and changeTaskStateUseCase returns status Travelling when onTaskClicked then statusChangeMessage is status_change_to_travelling`() {
        val vm = createViewModel(changeTaskStateUseCase = mock {
            on { onTaskClicked(3) } doReturn Single.just(Status.TRAVELLING)
        }).apply {
            uiState.value = Success(getAvailableTasks())
        }
        vm.onTaskClicked(3)
        val statusChangeMessage = vm.statusChangeMessage()
        statusChangeMessage.observeForever(mock())
        expectThat(statusChangeMessage.value).isEqualTo(R.string.status_change_to_travelling)
    }

    @Test
    fun `given uiState is Success and changeTaskStateUseCase returns status Working when onTaskClicked then statusChangeMessage is status_change_to_working`() {
        val vm = createViewModel(changeTaskStateUseCase = mock {
            on { onTaskClicked(3) } doReturn Single.just(Status.WORKING)
        }).apply {
            uiState.value = Success(getAvailableTasks())
        }
        val statusChangeMessage = vm.statusChangeMessage()
        vm.onTaskClicked(3)
        statusChangeMessage.observeForever(mock())
        expectThat(statusChangeMessage.value).isEqualTo(R.string.status_change_to_working)
    }

    @Test
    fun `given uiState is Success and changeTaskStateUseCase returns status Available when onTaskClicked then statusChangeMessage is status_change_to_available`() {
        val vm = createViewModel(changeTaskStateUseCase = mock {
            on { onTaskClicked(3) } doReturn Single.just(Status.AVAILABLE)
        }).apply {
            uiState.value = Success(getAvailableTasks())
        }
        vm.onTaskClicked(3)
        val statusChangeMessage = vm.statusChangeMessage()
        statusChangeMessage.observeForever(mock())
        expectThat(statusChangeMessage.value).isEqualTo(R.string.status_change_to_available)
    }

    @Test
    fun `given _statusChangeMessage hasn't changed and statusChangeMessage() has been called once already when statusChangeMessage() called again then it's value is null`() {
        val vm = createViewModel(changeTaskStateUseCase = mock {
            on { onTaskClicked(3) } doReturn Single.just(Status.TRAVELLING)
        }).apply {
            uiState.value = Success(getAvailableTasks())
        }
        vm.onTaskClicked(3)
        val ld1 = vm.statusChangeMessage()
        ld1.observeForever(mock())
        expectThat(ld1.value).isEqualTo(R.string.status_change_to_travelling)
        val ld2 = vm.statusChangeMessage()
        ld2.observeForever(mock())
        expectThat(ld2.value).isEqualTo(null)
    }


//

    private fun createViewModel(
        getAllTasksUseCase: GetAllTasksUseCase = mock {
            on { getAllTasks() } doReturn Observable.never()
        },
        changeTaskStateUseCase: ChangeTaskStateUseCase = mock(),
        schedulerProvider: SchedulerProvider = mock {
            on { io() } doReturn Schedulers.trampoline()
            on { ui() } doReturn Schedulers.trampoline()
        }
    ): TasksViewModel {
        return TasksViewModel(getAllTasksUseCase, changeTaskStateUseCase, schedulerProvider)
    }

    private fun getAvailableTasks(size: Int = 3): MutableList<TaskViewData> {
        val tasks = mutableListOf<TaskViewData>()
        for (i in 0..size) {
            val task = TaskViewData(i, "task ${i + 1}", Status.AVAILABLE)
            tasks.add(task)
        }
        return tasks
    }

    private fun getTasksWithOneHavingDifferentStatus(
        otherStatus: Status,
        otherIndex: Int = 0,
        size: Int = 3
    ): MutableList<TaskViewData> {
        val tasks = mutableListOf<TaskViewData>()
        for (i in 0..size) {
            val task = when (i) {
                otherIndex -> TaskViewData(i, "task ${i + 1}", otherStatus)
                else -> TaskViewData(i, "task ${i + 1}", Status.AVAILABLE)
            }
            tasks.add(task)
        }
        return tasks
    }

}