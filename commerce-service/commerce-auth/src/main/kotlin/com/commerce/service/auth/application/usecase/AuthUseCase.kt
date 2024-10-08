package com.commerce.service.auth.application.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.command.LoginCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.command.UpdateCommand
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto

interface AuthUseCase {

    fun login(command: LoginCommand): LoginInfoDto

    fun signUp(command: SignUpCommand)

    fun refresh(refreshToken: String): LoginInfoDto

    fun passwordVerify(member: Member, password: String): String

    fun update(member: Member, token: String, command: UpdateCommand)

    fun withdrawal(member: Member)
}