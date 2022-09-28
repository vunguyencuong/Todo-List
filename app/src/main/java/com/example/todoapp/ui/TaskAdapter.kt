package com.example.todoapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.TaskItem
import com.example.todoapp.databinding.ItemLayoutBinding
import com.example.todoapp.util.strikeThrough



class TaskAdapter(val clickListener: TaskClickListener, val onClickDone: (TaskItem) -> Unit) :
    ListAdapter<TaskItem, TaskAdapter.ViewHolder>(TaskDiffCallback) {


    companion object TaskDiffCallback : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem) =
            oldItem.tId == newItem.tId

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem) = oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(taskItem: TaskItem, clickListener: TaskClickListener) {
            binding.taskItem = taskItem
            binding.checkDone.isChecked = taskItem.isDone == 1
            binding.taskTitle.strikeThrough(taskItem.isDone == 1)
            binding.clickListener = clickListener
            binding.executePendingBindings()

            binding.checkDone.setOnClickListener { view ->
//                taskItem.isDone = if((view as CompoundButton).isChecked) 1 else 0
//                onClickDone(taskItem)
                Log.i("CheckBox", "Checkbox: update ")
                if ((view as CompoundButton).isChecked) {
                    binding.taskTitle.strikeThrough(true)
                    taskItem.isDone = 1
                } else {
                    binding.taskTitle.strikeThrough(false)
                    taskItem.isDone = 0
                }
                onClickDone(taskItem)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
    }


    fun setData(tasks: List<TaskItem>) {
        submitList(tasks.toList())
    }


}

class TaskClickListener(val clickListener: (taskItem: TaskItem) -> Unit) {
    fun onClick(taskItem: TaskItem) = clickListener(taskItem)

}


