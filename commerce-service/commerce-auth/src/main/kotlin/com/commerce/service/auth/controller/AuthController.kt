package com.commerce.service.auth.controller

import com.commerce.service.auth.application.usecase.AuthUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @GetMapping("/auth/hello")
    fun hello(): String {
        return authUseCase.findMember(1).toString()
    }
}