package com.commerce.common.model.member

interface MemberPasswordAuthRepository {

    fun exist(memberId: Long, token: String): Boolean
    fun setToken(memberId: Long, token: String)
}