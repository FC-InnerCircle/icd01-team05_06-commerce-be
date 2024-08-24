package com.commerce.service.auth.controller

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginMemberInfoDto
import com.commerce.service.auth.controller.common.BaseResponse
import com.commerce.service.auth.controller.request.SignInRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: SignInRequest): LoginInfoDto {
        return authUseCase.login(request.toCommand())
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): BaseResponse {
        authUseCase.signUp(request.toCommand())
        return BaseResponse.success()
    }

    @PutMapping("/update")
    fun update(@AuthenticationPrincipal member: Member, @RequestBody request: UpdateRequest): LoginMemberInfoDto {
        return authUseCase.update(member, request.toCommand())
    }

    @DeleteMapping("/withdrawal")
    fun withdrawal(@AuthenticationPrincipal member: Member): BaseResponse {
        authUseCase.withdrawal(member)
        return BaseResponse.success()
    }
}