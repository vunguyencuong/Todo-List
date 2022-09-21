package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Query
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.TaskItem
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository : TaskRepository
    private var isHidden = 0

    val getAllTasks: MutableLiveData<List<TaskItem>>
    val getAllPriorityTasks: LiveData<List<TaskItem>>

    init{
        repository = TaskRepository(taskDao)
        getAllTasks = repository.getALlTasks(isHidden) as MutableLiveData<List<TaskItem>>
        getAllPriorityTasks = repository.getAllPriorityTasks()

    }

    fun updateHidden(isDone: Int){
        isHidden = isDone
        updateList()
    }

    fun updateList(){
        getAllTasks.value = repository.getALlTasks(isHidden).value
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

    fun searchDatabase(searchQuery: String) : LiveData<List<TaskItem>>{
        return repository.searchDatabase(searchQuery)
    }

}