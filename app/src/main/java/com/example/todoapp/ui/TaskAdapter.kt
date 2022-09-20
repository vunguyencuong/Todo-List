package com.example.todoapp.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.database.TaskItem
import com.example.todoapp.databinding.ItemLayoutBinding
import com.example.todoapp.util.strikeThrough
import com.google.android.play.core.tasks.Task

class TaskAdapter(val clickListener: TaskClickListener, val onClickDone:(TaskItem)-> Unit): ListAdapter<TaskItem, TaskAdapter.ViewHolder>(TaskDiffCallback) {




    companion object TaskDiffCallback : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem) = oldItem.tId == newItem.tId
        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem) = oldItem == newItem
    }

    class ViewHolder(val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(taskItem: TaskItem, clickListener: TaskClickListener){
            binding.taskItem = taskItem
            binding.clickListener = clickListener
            binding.executePendingBindings()
            binding.checkDone.isChecked = taskItem.isDone
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,clickListener)
        holder.binding.checkDone.setOnClickListener { view ->
            if((view as CompoundButton).isChecked){
                current.isDone = true
                sendData(current)
                notifyItemChanged(position)
            } else{
                current.isDone = false
                sendData(current)
                notifyItemChanged(position)
            }
        }
        if(holder.binding.checkDone.isChecked){
            holder.binding.taskTitle.strikeThrough(true)
        } else holder.binding.taskTitle.strikeThrough(false)


    }


    private fun sendData(taskItem: TaskItem){
        onClickDone(taskItem)
    }




}

class TaskClickListener(val clickListener: (taskItem : TaskItem) -> Unit){
    fun onClick(taskItem: TaskItem) = clickListener(taskItem)

}


