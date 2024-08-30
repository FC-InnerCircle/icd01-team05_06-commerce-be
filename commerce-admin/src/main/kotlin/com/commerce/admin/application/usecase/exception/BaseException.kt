package com.commerce.admin.application.usecase.exception

import org.springframework.http.ProblemDetail

open class BaseException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause) {
    fun toProblemDetail(): ProblemDetail =
        errorCode.toProblemDetail().apply {
            detail = message ?: errorCode.message
        }
}
