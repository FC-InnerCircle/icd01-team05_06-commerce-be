package com.commerce.service.auth.application.usecase

import com.commerce.service.auth.application.usecase.command.SignInCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand

interface AuthUseCase {

    fun login(command: SignInCommand)

    fun signUp(command: SignUpCommand)
}