package com.commerce.common.model.member

interface MemberRepository {

    fun findById(id: Long): Member?
}