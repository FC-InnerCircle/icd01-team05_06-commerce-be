package com.commerce.admin.application.usecase.dto

import com.commerce.admin.application.usecase.exception.ErrorCode
import java.time.LocalDateTime

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun <T> success(
            data: T?,
            message: String = "Success",
        ): ApiResponse<T> = ApiResponse("SUCCESS", message, data)

        fun error(
            errorCode: ErrorCode,
            customMessage: String? = null,
        ): ApiResponse<ErrorResponse> =
            ApiResponse(
                status = "ERROR",
                message = customMessage ?: errorCode.message,
                data = ErrorResponse(errorCode.code, errorCode.message, errorCode.httpStatus.value()),
            )
    }
}
