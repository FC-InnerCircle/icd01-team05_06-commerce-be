package com.commerce.admin.application.usecase.exception

import com.commerce.admin.application.usecase.dto.ApiResponse
import com.commerce.admin.application.usecase.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

// ProblemDetail 응답 대신 ApiResponse로 응답 수정 24.08.31
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorResponse =
            ErrorResponse(
                code = ex.errorCode.code,
                message = ex.errorCode.message,
                status = ex.errorCode.httpStatus.value(),
            )
        return ResponseEntity(
            ApiResponse.error(ex.errorCode, ex.message ?: ex.errorCode.message),
            ex.errorCode.httpStatus,
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<ErrorResponse>> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val errorResponse =
            ErrorResponse(
                code = errorCode.code,
                message = errorCode.message,
                status = errorCode.httpStatus.value(),
            )
        return ResponseEntity(
            ApiResponse.error(errorCode, "An unexpected error occurred"),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
