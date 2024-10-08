package com.commerce.common.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<CommonResponse<Unit>> {
        val errorResponse = ErrorResponse("HEADER_NOT_FOUND_ERROR", "Invalid Header format")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error(listOf(errorResponse)))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonParseException(ex: HttpMessageNotReadableException): ResponseEntity<CommonResponse<Unit>> {
        val errorResponse = ErrorResponse("JSON_PARSE_ERROR", "Invalid JSON format")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error(listOf(errorResponse)))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Unit>> {
        val errors = ex.bindingResult.allErrors.map { error ->
            ErrorResponse("VALIDATION_ERROR", error.defaultMessage ?: "Validation error")
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error(errors))
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<CommonResponse<Unit>> {
        val errorResponse = ErrorResponse(ex.errorCode.code, ex.errorCode.message)

        return ResponseEntity
            .status(ex.httpStatus)
            .body(CommonResponse.error(listOf(errorResponse)))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<CommonResponse<Unit>> {
        val errorResponse = ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.code, ErrorCode.INTERNAL_SERVER_ERROR.message)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CommonResponse.error(listOf(errorResponse)))
    }
}