package com.example.nayanepal.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: String? = null) : Result<Nothing>()
}
data class PaginatedResponse<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Long,
    val error: String? = null
)
