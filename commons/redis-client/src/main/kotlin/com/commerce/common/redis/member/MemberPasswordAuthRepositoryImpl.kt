package com.commerce.common.redis.member

import com.commerce.common.model.member.MemberPasswordAuthRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

const val KEY = "member.password.auth-token"

@Repository
class MemberPasswordAuthRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : MemberPasswordAuthRepository {

    override fun exist(memberId: Long, token: String): Boolean {
        return redisTemplate.hasKey("$KEY:$memberId:$token")
    }

    override fun setToken(memberId: Long, token: String) {
        redisTemplate.opsForValue().set("$KEY:$memberId:$token", token, 10, TimeUnit.MINUTES)
    }
}