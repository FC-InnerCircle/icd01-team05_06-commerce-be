package com.commerce.service.order.applicaton.usecase.exception

import com.commerce.service.order.controller.common.responese.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<CommonResponse<Unit>> {
        val status = when (ex) {
            is InvalidInputException -> HttpStatus.BAD_REQUEST
            is OrderNotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = ErrorResponse(ex.errorCode.code, ex.errorCode.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse(success = false, error = errorResponse))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<CommonResponse<Unit>> {
        val errorResponse = ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.code, ErrorCode.INTERNAL_SERVER_ERROR.message)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CommonResponse(success = false, error = errorResponse))
    }
}