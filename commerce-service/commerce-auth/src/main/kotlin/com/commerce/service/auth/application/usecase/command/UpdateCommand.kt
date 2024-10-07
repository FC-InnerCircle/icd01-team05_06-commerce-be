package com.commerce.service.auth.application.usecase.command

import com.commerce.common.model.address.Address

data class UpdateCommand(
    val password: String?,
    val name: String,
    val phone: String,
    val address: Address
)
