package com.example.simpletask_kotlin.ui.tasks

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.simpletask_kotlin.data.ui.InProgress
import com.example.simpletask_kotlin.data.ui.Success
import com.example.simpletask_kotlin.data.ui.TasksUiState
import com.example.simpletask_kotlin.data.ui.Error as UiStateError

@BindingAdapter("uiState")
fun setUiStateForLoading(progressView: ProgressBar, uiState: TasksUiState) {
    progressView.visibility = when (uiState) {
        InProgress -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("uiState")
fun setUiStateForLoadedContent(view: View, uiState: TasksUiState) {
    view.visibility = when (uiState) {
        is Success -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("errorState")
fun setUiStateForErrorView(view: View, uiState: TasksUiState) {
    view.visibility = when (uiState) {
        is Error -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("errorTextState")
fun setUiStateForErrorText(textView: TextView, uiState: TasksUiState) {
    val textState = when (uiState) {
        is UiStateError -> uiState.message to View.VISIBLE
        else -> "" to View.GONE
    }

    textView.text = textState.first
    textView.visibility = textState.second
}

