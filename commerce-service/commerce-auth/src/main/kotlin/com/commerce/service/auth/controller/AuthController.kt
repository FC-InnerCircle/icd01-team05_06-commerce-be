package com.commerce.service.auth.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.common.response.ErrorCode
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.MemberInfoDto
import com.commerce.service.auth.application.usecase.exception.AuthException
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): CommonResponse<LoginInfoDto> {
        return CommonResponse.ok(authUseCase.login(request.toCommand()))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): CommonResponse<Unit> {
        authUseCase.signUp(request.toCommand())
        return CommonResponse.ok()
    }

    @GetMapping("/info")
    fun info(@AuthenticationPrincipal member: Member): CommonResponse<MemberInfoDto> {
        val memberInfo = MemberInfoDto(
            id = member.id,
            name = member.name,
            email = member.email,
            phone = member.phone,
        )
        return CommonResponse.ok(memberInfo)
    }

    @PostMapping("/refresh")
    fun refresh(request: HttpServletRequest): CommonResponse<LoginInfoDto> {
        val authHeader = request.getHeader("refresh-token")
        if (authHeader?.startsWith("Bearer ") != true) {
            throw AuthException(ErrorCode.PERMISSION_ERROR)
        }

        val refreshToken = authHeader.substring(7)

        return CommonResponse.ok(authUseCase.refresh(refreshToken))
    }

    @PutMapping("/update")
    fun update(
        @AuthenticationPrincipal member: Member,
        @RequestBody request: UpdateRequest
    ): CommonResponse<Unit> {
        authUseCase.update(member, request.toCommand())
        return CommonResponse.ok()
    }

    @DeleteMapping("/withdrawal")
    fun withdrawal(@AuthenticationPrincipal member: Member): CommonResponse<Unit> {
        authUseCase.withdrawal(member)
        return CommonResponse.ok()
    }
}