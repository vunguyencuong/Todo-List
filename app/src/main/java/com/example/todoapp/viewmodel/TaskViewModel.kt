package com.example.todoapp.viewmodel

import android.app.Application
import android.icu.text.CaseMap
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.database.TaskItem
import com.example.todoapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {



    private val taskDao = TaskDatabase.getDatabase(application).taskDao()
    private val repository  = TaskRepository(taskDao)
    private var isHidden = 0
    var isArchived  = 0

    private lateinit var  taskList : MutableList<TaskItem>
    private val _taskLiveData by lazy {
        MutableLiveData<List<TaskItem>>().also {
            updateList()
        }
    }

    val taskLiveData : LiveData<List<TaskItem>> = _taskLiveData


//
//    fun setDoneItemVisibility(isDone: Int){
//       // Log.i("Hidden check", "updateHidden: update")
//        isHidden = isDone
//        updateLiveData()
//    }



    fun updateList(){
        viewModelScope.launch(Dispatchers.IO) {
            taskList = repository.getALlTasks().toMutableList()
            updateLiveData()
            isArchived = 0
        }
    }

    fun updateRecycleBinList(){
        Log.i("BugBin", "updateRecycleBinList: ")
        viewModelScope.launch(Dispatchers.IO){
            taskList = repository.getAllTasksMoveToRecycleBin().toMutableList()
            updateLiveData()
            isArchived = 1
        }
    }

    fun updateLiveData(){
        viewModelScope.launch {
            Log.i("BugBin", "updateLiveData: ")
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
        Log.i("BugBin", "addTask: ${isArchived}")
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

    fun moveDoneTaskToRecycleBin(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO){
            Log.i("Update Archived", "moveDoneTaskToRecycleBin: ${taskItem.isArchived}")
            taskItem.isArchived = 1
            repository.updateTask(taskItem)
            taskList.remove(taskItem)
            updateLiveData()

        }
    }

    fun updateTask(newTaskItem: TaskItem){

        Log.i("Checkbox", "updateTask: ${newTaskItem.tId}")
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

    fun unArchivedTask(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO){
           // taskItem.isArchived = 0
            repository.updateTask(taskItem)
            taskList.remove(taskItem)
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

//    fun getHidden() : Int{
//        return isHidden
//    }

    fun searchTitle(str : String){
        Log.d("Title", "searchTitle: ")
        val filtered = taskList.filter {
            it.title.contains(str,true)
        }
        _taskLiveData.value = filtered
    }



}