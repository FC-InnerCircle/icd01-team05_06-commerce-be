package com.mock.service.auth.application.usecase

import com.commerce.service.auth.application.service.TokenType
import com.commerce.service.auth.application.usecase.TokenUseCase

class FakeTokenUseCase : TokenUseCase {

    override fun createToken(memberId: Long, tokenType: TokenType): String {
        return memberId.toString() + tokenType
    }
}