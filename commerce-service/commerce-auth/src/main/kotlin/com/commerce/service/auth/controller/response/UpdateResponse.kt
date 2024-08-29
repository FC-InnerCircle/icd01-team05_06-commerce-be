package com.commerce.service.auth.controller.response

import com.commerce.service.auth.application.usecase.dto.LoginMemberInfoDto

data class UpdateResponse(
    val memberInfo: LoginMemberInfoDto,
)
