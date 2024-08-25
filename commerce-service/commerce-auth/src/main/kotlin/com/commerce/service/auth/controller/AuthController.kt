package com.commerce.service.auth.controller

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.exception.AuthException
import com.commerce.service.auth.controller.common.BaseResponse
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import com.commerce.service.auth.controller.response.AccessTokenResponse
import com.commerce.service.auth.controller.response.UpdateResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginInfoDto {
        return authUseCase.login(request.toCommand())
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): BaseResponse {
        authUseCase.signUp(request.toCommand())
        return BaseResponse.success()
    }

    @PostMapping("/refresh")
    fun refresh(request: HttpServletRequest): AccessTokenResponse {
        val authHeader = request.getHeader("refresh-token")
        if (authHeader?.startsWith("Bearer ") != true) {
            throw AuthException("권한이 없습니다.")
        }

        val refreshToken = authHeader.substring(7)

        val accessToken = authUseCase.refresh(refreshToken)
        return AccessTokenResponse(accessToken = accessToken)
    }

    @PutMapping("/update")
    fun update(@AuthenticationPrincipal member: Member, @RequestBody request: UpdateRequest): UpdateResponse {
        return UpdateResponse(authUseCase.update(member, request.toCommand()))
    }

    @DeleteMapping("/withdrawal")
    fun withdrawal(@AuthenticationPrincipal member: Member): BaseResponse {
        authUseCase.withdrawal(member)
        return BaseResponse.success()
    }
}