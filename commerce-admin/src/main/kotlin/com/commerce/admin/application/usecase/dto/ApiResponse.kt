package com.commerce.admin.application.usecase.dto

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

        fun <T> error(
            message: String,
            data: T? = null,
        ): ApiResponse<T> = ApiResponse("ERROR", message, data)
    }
}
