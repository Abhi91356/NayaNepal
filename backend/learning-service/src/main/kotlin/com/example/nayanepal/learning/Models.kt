package com.example.nayanepal.learning

import java.time.Instant
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val skillTags: List<String>,
    val difficulty: String // beginner, intermediate, advanced
)
data class Module(
    val courseId: String,
    val content: String,
    val skillTags: List<String>
data class Enrollment(
    val userId: String,
    val enrolledAt: Instant,
    val status: String
data class LearningProgress(
    val moduleId: String,
    val completedAt: Instant,
    val score: Int,
    val timeSpent: Long,
data class ModuleCompletionRequest(
    val timeSpent: Long
