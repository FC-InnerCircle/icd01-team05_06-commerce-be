package com.commerce.service.auth.application.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class AuthException(
    errorCode: ErrorCode
) :
    CustomException(
        HttpStatus.BAD_REQUEST,
        errorCode
    )