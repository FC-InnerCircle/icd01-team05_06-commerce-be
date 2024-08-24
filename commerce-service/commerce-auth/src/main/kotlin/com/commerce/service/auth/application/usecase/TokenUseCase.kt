package com.commerce.service.auth.application.usecase

import com.commerce.service.auth.application.service.TokenType

interface TokenUseCase {

    fun getTokenSubject(token: String, tokenType: TokenType): String?

    fun createToken(memberId: Long, tokenType: TokenType): String
}