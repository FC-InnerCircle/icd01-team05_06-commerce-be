package com.commerce.service.auth.application.usecase.command

data class UpdateCommand(
    val password: String?,
    val name: String,
    val phone: String,
)
