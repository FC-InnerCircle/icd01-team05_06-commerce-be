package com.commerce.service.auth.application.usecase

import com.commerce.service.auth.application.service.TokenType

interface TokenUseCase {

    fun createToken(memberId: Long, tokenType: TokenType): String
}