package com.mock.common.jwt.application.usecase

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenDto
import com.commerce.common.jwt.application.usecase.TokenUseCase

class FakeTokenUseCase : TokenUseCase {

    val data = mutableMapOf<String, Long>()

    override fun getTokenSubject(token: String, tokenType: TokenType): String? {
        return data[token]?.toString()
    }

    override fun createToken(memberId: Long, tokenType: TokenType): TokenDto {
        val token = "${tokenType}_${memberId}"
        data[token] = memberId
        return TokenDto(
            token = token,
            expiresIn = System.currentTimeMillis() + tokenType.validTime.inWholeMilliseconds
        )
    }
}