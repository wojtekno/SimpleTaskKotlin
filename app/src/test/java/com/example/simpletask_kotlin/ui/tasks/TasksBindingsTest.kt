package com.example.simpletask_kotlin.ui.tasks

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.simpletask_kotlin.data.ui.Success
import com.example.simpletask_kotlin.data.ui.TasksUiState

class TasksBindingsTest {


    @BindingAdapter("uiState")
    fun setUiStateForLoadedContent(view: View, uiState: TasksUiState) {
        view.visibility = when (uiState) {
            is Success -> View.VISIBLE
            else -> View.GONE
        }
    }

}