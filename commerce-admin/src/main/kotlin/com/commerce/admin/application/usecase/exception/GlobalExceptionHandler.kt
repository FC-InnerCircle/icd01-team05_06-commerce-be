package com.commerce.admin.application.usecase.exception

import com.commerce.admin.application.usecase.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ApiResponse<ProblemDetail>> {
        val problemDetail = ex.toProblemDetail()
        return ResponseEntity(ApiResponse.error(ex.message ?: ex.errorCode.message, problemDetail), ex.errorCode.httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<ProblemDetail>> {
        val problemDetail = ErrorCode.INTERNAL_SERVER_ERROR.toProblemDetail()
        return ResponseEntity(ApiResponse.error("An unexpected error occurred", problemDetail), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
