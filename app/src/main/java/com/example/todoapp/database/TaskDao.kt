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

    @Query("DELETE FROM tasks_table where isArchived = 1")
    suspend fun deleteAll()

    @Query("SELECT * FROM tasks_table where isArchived = 0 ORDER BY timestamp DESC")
    fun getAllTasks(): List<TaskItem>

    @Query("select * from tasks_table  order by priority asc")
    fun getAllPriorityTasks(): List<TaskItem>

//    @Query("select * from tasks_table where title like :searchQuery order by timestamp desc")
//    fun searchDatabase(searchQuery: String): List<TaskItem>
    @Query("select * from tasks_table where isArchived = 1")
    fun getAllTasksMoveToRecycleBin() : List<TaskItem>

}