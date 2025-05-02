package com.rudiridho.catalogapps.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import javax.net.ssl.SSLPeerUnverifiedException

const val STATUS = "status"
const val STATUS_CODE = "statusCode"
const val STATUS_CODE_404 = 404

suspend fun <Input, Output> processResponse(
    result: NetworkResultWrapper<Input>,
    successBlock: suspend (Input) -> DomainResult.Success<Output>,
): DomainResult<Output> {
    return when (result) {
        is NetworkResultWrapper.Error -> errorMapper(result)
        is NetworkResultWrapper.Exception -> exceptionMapper(result)
        is NetworkResultWrapper.Success -> successBlock(result.data)
    }
}

fun <Input, Output> errorMapper(result: NetworkResultWrapper.Error<Output>): DomainResult<Input> {
    val emptyStateErrorCodes = listOf(422, 404, 403, 409, 406, 401, 400)

    return if (emptyStateErrorCodes.contains(result.code)) DomainResult.EmptyState(
        result.message,
        getJsonElement(result.data, STATUS_CODE, STATUS, result.code) { it.asInt },
        result.code
    )
    else DomainResult.ErrorState(
        result.message,
        getJsonElement(result.data, STATUS_CODE, STATUS, result.code) { it.asInt }
    )
}

fun <Input, Output> exceptionMapper(result: NetworkResultWrapper.Exception<Output>): DomainResult<Input> {
    return when (result.throwable) {
        is SSLPeerUnverifiedException -> DomainResult.TechnicalError(SSL_ERROR_CONST)
        is JsonSyntaxException -> DomainResult.TechnicalError(RESPONSE_ERROR_CONST)
        else -> DomainResult.NetworkError
    }
}

internal fun <T> getJsonElement(
    jsonObject: JsonObject,
    key: String,
    key2: String,
    defaultValue: T,
    transform: (JsonElement) -> T
): T {
    return try {
        when {
            jsonObject.has(key) -> transform(jsonObject.get(key))
            jsonObject.has(key2) -> transform(jsonObject.get(key2))
            else -> defaultValue
        }
    } catch (e: Exception) {
        defaultValue
    }
}