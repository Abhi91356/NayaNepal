package com.example.nayanepal.data.model.learning

data class Course(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val level: String = "", // beginner, intermediate, advanced
    val totalModules: Int = 0,
    val durationMinutes: Int = 0
)
