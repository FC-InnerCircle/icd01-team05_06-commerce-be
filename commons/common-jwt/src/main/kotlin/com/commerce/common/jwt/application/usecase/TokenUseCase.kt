package com.commerce.common.jwt.application.usecase

import com.commerce.common.jwt.application.service.TokenType

interface TokenUseCase {

    fun getTokenSubject(token: String, tokenType: TokenType): String?

    fun createToken(memberId: Long, tokenType: TokenType): String
}