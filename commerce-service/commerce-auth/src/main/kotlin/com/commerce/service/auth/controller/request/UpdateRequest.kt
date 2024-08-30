package com.commerce.service.auth.controller.request

import com.commerce.service.auth.application.usecase.command.UpdateCommand

data class UpdateRequest(
    val password: String?,
    val name: String,
    val phone: String,
) {
    fun toCommand() = UpdateCommand(
        password = password,
        name = name,
        phone = phone
    )
}
