package com.example.todoapp.database

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(taskItem: TaskItem)

    @Delete
    suspend fun deleteTask(taskItem: TaskItem)

    @Update
    suspend fun updateTask(taskItem: TaskItem)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM tasks_table  ORDER BY timestamp DESC")
    fun getAllTasks(): LiveData<List<TaskItem>>

    @Query("select * from tasks_table  order by priority asc")
    fun getAllPriorityTasks(): LiveData<List<TaskItem>>

    @Query("select * from tasks_table where title like :searchQuery order by timestamp desc")
    fun searchDatabase(searchQuery: String): LiveData<List<TaskItem>>

    @Query("select * from tasks_table where isDone = 0")
    fun readNotDoneData(): LiveData<MutableList<TaskItem>>


}