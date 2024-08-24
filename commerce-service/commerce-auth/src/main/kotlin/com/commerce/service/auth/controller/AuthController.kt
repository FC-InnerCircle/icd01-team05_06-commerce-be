package com.commerce.service.auth.controller

import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.controller.common.BaseResponse
import com.commerce.service.auth.controller.request.SignInRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: SignInRequest): BaseResponse {
        authUseCase.login(request.toCommand())
        return BaseResponse.success()
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): BaseResponse {
        authUseCase.signUp(request.toCommand())
        return BaseResponse.success()
    }
}