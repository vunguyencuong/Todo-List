package com.example.todoapp.database

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks_table ORDER BY timestamp DESC")
    fun getAllTasks(): LiveData<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(taskItem: TaskItem)

    @Delete
    suspend fun deleteTask(taskItem: TaskItem)

    @Update
    suspend fun updateTask(taskItem: TaskItem)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAll()

}