package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.TaskItem
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository : TaskRepository

    val getAllTasks: LiveData<List<TaskItem>>

    init{
        repository = TaskRepository(taskDao)
        getAllTasks = repository.getALlTasks()
    }

    fun addTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(taskItem)
        }
    }

    fun deleteTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTask(taskItem)
        }
    }

    fun updateTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskItem)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

}