package com.commerce.service.auth.application.usecase

import com.commerce.service.auth.application.usecase.command.SignUpCommand

interface AuthUseCase {

    fun signUp(command: SignUpCommand)
}