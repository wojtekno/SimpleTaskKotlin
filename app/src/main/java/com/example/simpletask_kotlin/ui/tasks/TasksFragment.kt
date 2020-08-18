package com.example.simpletask_kotlin.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simpletask_kotlin.MyApplication
import com.example.simpletask_kotlin.data.ui.Error
import com.example.simpletask_kotlin.data.ui.InProgress
import com.example.simpletask_kotlin.data.ui.Success
import com.example.simpletask_kotlin.data.ui.TasksDisplayable
import com.example.simpletask_kotlin.databinding.MainFragmentBinding

class TasksFragment : Fragment() {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
//        return inflater.inflate(R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory =
            (requireActivity().application as MyApplication).appGraph.tasksViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(TasksViewModel::class.java)
        viewModel.uiState.observe(viewLifecycleOwner, Observer<TasksDisplayable> {
            binding.errorMessageTv.text = when (it) {
                is Success -> "success"
                is InProgress -> "in progress"
                is Error -> "error"
                else -> "other scenario"
            }

            when (it) {
                is InProgress -> binding.progressBar.visibility = View.VISIBLE
                else -> binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

}

