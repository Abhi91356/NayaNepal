package com.example.nayanepal.data.model.agriculture

data class WeatherAlert(
    val id: String = "",
    val title: String = "",
    val severity: String = "info", // info, warning, danger
    val location: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
