package com.rudiridho.catalogapps.utils

const val SSL_ERROR_CONST = 900
const val RESPONSE_ERROR_CONST = 901
const val CODE_400 = 400

sealed class DomainResult<out T> {data class Success<T>(val data: T) : DomainResult<T>()
    data class TechnicalError(val code: Int, val message: String? = null) : DomainResult<Nothing>()
    data class EmptyState(val message: String? = null, val responseStatusCode: Int? = 0,  val statusCode: Int? = 0) :
        DomainResult<Nothing>()
    data class ErrorState<T>(
        val message: String? = "null",
        val responseStatusCode: Int? = 0,
        val data: T? = null
    ) : DomainResult<T>()
    object NetworkError : DomainResult<Nothing>()
}