package com.mock.service.auth.application.usecase

import com.commerce.service.auth.application.service.TokenType
import com.commerce.service.auth.application.usecase.TokenUseCase

class FakeTokenUseCase : TokenUseCase {

    val data = mutableMapOf<String, Long>()

    override fun getTokenSubject(token: String, tokenType: TokenType): String? {
        return data[token]?.toString()
    }

    override fun createToken(memberId: Long, tokenType: TokenType): String {
        val token = "${tokenType}_${memberId}"
        data[token] = memberId
        return token
    }
}