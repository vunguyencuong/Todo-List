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
            if(binding.checkDone.isChecked) taskItem.isDone = 1
            else taskItem.isDone = 0
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,clickListener)
        //holder.binding.checkDone.setOnCheckedChangeListener { _, b ->
//            if(b){
//                current.isDone = 1
//                holder.binding.taskTitle.strikeThrough(true)
//            } else{
//                current.isDone = 0
//                holder.binding.taskTitle.strikeThrough(false)
//            }
//
//            onClickDone(current)
//        }

        holder.binding.checkDone.setOnClickListener { view ->
            if((view as CompoundButton).isChecked){
                current.isDone = 1
                holder.binding.taskTitle.strikeThrough(true)
            } else{
                current.isDone = 0
                holder.binding.taskTitle.strikeThrough(false)
            }
            onClickDone(current)
        }


    }


    fun setData(tasks : List<TaskItem>){
        submitList(tasks.toList())
    }


}

class TaskClickListener(val clickListener: (taskItem : TaskItem) -> Unit){
    fun onClick(taskItem: TaskItem) = clickListener(taskItem)

}


