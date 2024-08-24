package com.mock.service.auth.application.usecase

import com.commerce.service.auth.application.service.TokenType
import com.commerce.service.auth.application.usecase.TokenUseCase

class FakeTokenUseCase : TokenUseCase {
    override fun getTokenSubject(token: String, tokenType: TokenType): String? {
        return token + tokenType
    }

    override fun createToken(memberId: Long, tokenType: TokenType): String {
        return memberId.toString() + tokenType
    }
}