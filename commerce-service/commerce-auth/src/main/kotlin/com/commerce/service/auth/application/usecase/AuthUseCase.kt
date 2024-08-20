package com.commerce.service.auth.application.usecase

import com.commerce.common.model.member.Member

interface AuthUseCase {

    fun findMember(id: Long): Member?
}