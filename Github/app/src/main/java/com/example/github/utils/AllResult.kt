package com.example.github.utils

sealed class AllResult {
    data class Success<out T>(val data: T) : AllResult()
    data class Error(val exception: Throwable) : AllResult()
    data class Loading(val isLoading: Boolean) : AllResult()
}
