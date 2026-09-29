package com.example.nayanepal.core.network.domain

interface TokenProvider {
    fun getToken(): String?
}
