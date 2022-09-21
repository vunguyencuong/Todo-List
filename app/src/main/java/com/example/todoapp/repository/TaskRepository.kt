package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.todoapp.database.TaskDao
import com.example.todoapp.database.TaskItem

class TaskRepository(val taskDao: TaskDao) {

    suspend fun addTask(taskItem: TaskItem) = taskDao.addTask(taskItem)

    suspend fun deleteTask(taskItem: TaskItem) = taskDao.deleteTask(taskItem)

    suspend fun updateTask(taskItem: TaskItem) = taskDao.updateTask(taskItem)

    suspend fun deleteAll(){
        taskDao.deleteAll()
    }

    fun getALlTasks(isDone: Int) : LiveData<List<TaskItem>> = taskDao.getAllTasks(isDone)

    fun getAllPriorityTasks() : LiveData<List<TaskItem>> = taskDao.getAllPriorityTasks()

    fun searchDatabase(searchQuery: String) : LiveData<List<TaskItem>>{
        return taskDao.searchDatabase(searchQuery)
    }



}