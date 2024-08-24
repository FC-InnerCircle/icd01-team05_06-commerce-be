package com.commerce.service.auth.application.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.command.SignInCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto

interface AuthUseCase {

    fun login(command: SignInCommand): LoginInfoDto

    fun signUp(command: SignUpCommand)

    fun withdrawal(member: Member)
}