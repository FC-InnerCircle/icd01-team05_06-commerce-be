package com.commerce.service.auth.controller.common

import com.commerce.service.auth.application.usecase.exception.AuthException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthException::class)
    fun authException(e: AuthException) = ErrorResponse(e.message!!)
}