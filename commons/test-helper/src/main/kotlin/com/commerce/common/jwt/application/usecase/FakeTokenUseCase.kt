package com.commerce.common.jwt.application.usecase

import com.commerce.common.jwt.application.service.TokenType

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