package com.example.nayanepal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface LearningDao {
    @Query("SELECT * FROM learning_modules WHERE careerPath = :path")
    suspend fun getModulesForPath(path: String): List<LearningModuleEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModules(modules: List<LearningModuleEntity>)
    @Query("UPDATE learning_modules SET progress = :progress WHERE id = :moduleId")
    suspend fun updateProgress(moduleId: String, progress: Int)
}
