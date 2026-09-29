package com.example.nayanepal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "learning_modules")
data class LearningModuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val contentUrl: String,
    val progress: Int = 0,
    val isCompleted: Boolean = false,
    val careerPath: String? = null
)
