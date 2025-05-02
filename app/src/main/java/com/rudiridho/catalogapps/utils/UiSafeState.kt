package com.rudiridho.catalogapps.utils

sealed class UiSafeState<out T> {
    object Uninitialized : UiSafeState<Nothing>() // stateless or default state
    object Loading : UiSafeState<Nothing>()
    object Empty : UiSafeState<Nothing>()
    object ErrorConnection : UiSafeState<Nothing>()
    data class Error<T>(
        val message: String? = null,
        val errorCode: Int? = null,
        val data: T? = null
    ) : UiSafeState<T>()

    data class Success<out T>(val data: T) : UiSafeState<T>()
}

fun <T> UiSafeState<T>.isError(withEmpty: Boolean = true): Boolean =
    this is UiSafeState.Error ||
            this is UiSafeState.ErrorConnection ||
            (this is UiSafeState.Empty && withEmpty)