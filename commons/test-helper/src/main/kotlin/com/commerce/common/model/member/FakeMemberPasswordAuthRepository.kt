package com.commerce.common.model.member

const val KEY = "member.password.auth-token"

class FakeMemberPasswordAuthRepository : MemberPasswordAuthRepository {

    var data = mutableListOf<String>()

    override fun exist(memberId: Long, token: String): Boolean {
        return data.any { it == "$KEY:$memberId:$token" }
    }

    override fun setToken(memberId: Long, token: String) {
        data.add("$KEY:$memberId:$token")
    }
}