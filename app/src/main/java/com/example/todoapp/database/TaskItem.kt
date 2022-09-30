package com.example.todoapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tasks_table")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) var tId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "isDone") var isDone: Int,
    @ColumnInfo(name = "isArchived") var isArchived: Int
) : Parcelable