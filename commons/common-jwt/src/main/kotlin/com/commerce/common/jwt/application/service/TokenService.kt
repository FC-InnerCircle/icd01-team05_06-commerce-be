package com.commerce.common.jwt.application.service

import com.commerce.common.jwt.application.usecase.TokenUseCase
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class TokenService : TokenUseCase {
    @Value("\${jwt.access-token-key}")
    private lateinit var accessTokenKey: String

    @Value("\${jwt.refresh-token-key}")
    private lateinit var refreshTokenKey: String

    private lateinit var accessTokenSigningKey: SecretKey
    private lateinit var refreshTokenSigningKey: SecretKey

    @PostConstruct
    private fun init() {
        this.accessTokenSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey))
        this.refreshTokenSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey))
    }

    private fun getSigningKey(tokenType: TokenType) =
        when (tokenType) {
            TokenType.ACCESS_TOKEN -> this.accessTokenSigningKey
            TokenType.REFRESH_TOKEN -> this.refreshTokenSigningKey
        }

    override fun getTokenSubject(token: String, tokenType: TokenType) =
        getClaims(token, tokenType)?.subject

    private fun getClaims(token: String, tokenType: TokenType) =
        try {
            Jwts.parser()
                .verifyWith(this.getSigningKey(tokenType))
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: JwtException) {
            null
        }

    override fun createToken(memberId: Long, tokenType: TokenType): String {
        val time = System.currentTimeMillis()

        return Jwts.builder()
            .subject(memberId.toString())
            .issuedAt(Date(time))
            .expiration(Date(time + tokenType.validTime.inWholeMilliseconds))
            .signWith(this.getSigningKey(tokenType))
            .compact()
    }
}
