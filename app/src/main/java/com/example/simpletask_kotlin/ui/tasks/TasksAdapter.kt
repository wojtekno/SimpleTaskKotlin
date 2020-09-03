package com.example.simpletask_kotlin.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletask_kotlin.data.ui.TaskViewData
import com.example.simpletask_kotlin.databinding.TaskItemBinding

class TasksAdapter(private val onTaskClickListener: OnTaskClickListener) :
    ListAdapter<TaskViewData, TasksAdapter.TaskViewHolder>(DiffUtilCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = currentList[position]
        holder.bindTask(task)
        holder.itemView.setOnClickListener(holder)

    }

    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        fun bindTask(task: TaskViewData) {
            binding.task = task
        }

        override fun onClick(p0: View?) {
            onTaskClickListener.onItemClick(adapterPosition)
        }


    }

    object DiffUtilCallback : DiffUtil.ItemCallback<TaskViewData>() {
        override fun areItemsTheSame(oldItem: TaskViewData, newItem: TaskViewData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskViewData, newItem: TaskViewData): Boolean =
            oldItem == newItem
    }


    interface OnTaskClickListener {
        fun onItemClick(listIndex: Int)
    }
}