package com.commerce.service.auth.controller

import com.commerce.service.auth.application.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {

    @GetMapping("/auth/hello")
    fun hello(): String {
        return authService.findMember(1).toString()
    }
}