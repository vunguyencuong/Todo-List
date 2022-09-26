package com.example.todoapp.viewmodel

import android.app.Application
import android.icu.text.CaseMap
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Query
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.TaskItem
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {



    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository  = TaskRepository(taskDao)
    private var isHidden = 0


    private lateinit var  taskList : MutableList<TaskItem>
    private val _taskLiveData by lazy {
        MutableLiveData<List<TaskItem>>().also {
            updateList()
        }
    }

    val taskLiveData : LiveData<List<TaskItem>> = _taskLiveData



    fun updateHidden(isDone: Int){
        isHidden = isDone
        updateLiveData()
    }

    fun updateList(){
        viewModelScope.launch(Dispatchers.IO) {
            taskList = repository.getALlTasks().toMutableList()
            updateLiveData()
        }
    }

    fun updateLiveData(){
        viewModelScope.launch {
            if(isHidden == 0){
                _taskLiveData.value = taskList
            } else _taskLiveData.value = filterNotDone()

        }
    }

    fun filterNotDone () : List<TaskItem> {
         return taskList.filter {
            it.isDone == 0
        }
    }

    fun addTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(taskItem)
            taskList.add(taskItem)
            updateLiveData()
        }
    }

    fun deleteTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTask(taskItem)
            taskList.remove(taskItem)
            updateLiveData()
        }
    }

    fun updateTask(newTaskItem: TaskItem){

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(newTaskItem)
            for(i in 0..taskList.size-1){
                if(taskList[i].tId == newTaskItem.tId){
                    taskList[i] = newTaskItem
                    break
                }
            }
            updateLiveData()
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            taskList = mutableListOf()
            updateLiveData()
        }
    }

    fun sortTask(){
        taskList.sortBy {
            it.priority
        }
        updateLiveData()
    }

    fun getHidden() : Int{
        return isHidden
    }

    fun searchTitle(str : String){
        Log.d("Title", "searchTitle: ")
        val filtered = taskList.filter {
            it.title.contains(str,true)
        }
        _taskLiveData.value = filtered
    }



}