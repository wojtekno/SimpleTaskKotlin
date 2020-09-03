package com.example.simpletask_kotlin.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletask_kotlin.MyApplication
import com.example.simpletask_kotlin.R
import com.example.simpletask_kotlin.data.ui.Success
import com.example.simpletask_kotlin.data.ui.TasksUiState
import com.example.simpletask_kotlin.databinding.MainFragmentBinding
import com.github.ajalt.timberkt.Timber

class TasksFragment : Fragment(), TasksAdapter.OnTaskClickListener {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var toast: Toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = TasksAdapter(this)
        binding.tasksRv.adapter = adapter
        binding.tasksRv.layoutManager = LinearLayoutManager(context)
        toast = Toast(context)
        binding.testBt.setOnClickListener {
            showToast(R.string.test_btn_clicked_text)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory =
            (requireActivity().application as MyApplication).appGraph.tasksViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(TasksViewModel::class.java)
        binding.vm = viewModel
        viewModel.uiState.observe(viewLifecycleOwner, Observer<TasksUiState> {
            when (it) {
                is Success -> adapter.submitList(it.tasks)
            }
        })
        viewModel.statusChangeMessage().observe(viewLifecycleOwner) { message ->
            message?.let { showToast(it) }
        }
    }

    override fun onItemClick(listIndex: Int) {
        viewModel.onTaskClicked(listIndex)
    }

    private fun showToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        toast.cancel()
        toast = Toast.makeText(context, resId, duration)
        toast.show()
    }

}

