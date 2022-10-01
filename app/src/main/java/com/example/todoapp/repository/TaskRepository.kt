package com.example.todoapp.repository

import com.example.todoapp.database.TaskDao
import com.example.todoapp.database.TaskItem

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun addTask(taskItem: TaskItem) = taskDao.addTask(taskItem)

    suspend fun deleteTask(taskItem: TaskItem) = taskDao.deleteTask(taskItem)

    suspend fun updateTask(taskItem: TaskItem) = taskDao.updateTask(taskItem)

    suspend fun deleteAll(){
        taskDao.deleteAll()
    }

    fun getALlTasks() : List<TaskItem> = taskDao.getAllTasks()

  //  fun getAllPriorityTasks() : List<TaskItem> = taskDao.getAllPriorityTasks()

//    fun searchDatabase(searchQuery: String) : List<TaskItem>{
//        return taskDao.searchDatabase(searchQuery)
//    }

    fun getAllTasksMoveToRecycleBin() : List<TaskItem> = taskDao.getAllTasksMoveToRecycleBin()


}