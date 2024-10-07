package com.commerce.service.auth.controller.request

import com.commerce.common.model.address.Address
import com.commerce.service.auth.application.usecase.command.UpdateCommand

data class UpdateRequest(
    val password: String?,
    val name: String,
    val phone: String,
    val postalCode: String,
    val streetAddress: String,
    val detailAddress: String,
) {
    fun toCommand() = UpdateCommand(
        password = password,
        name = name,
        phone = phone,
        address = Address(
            postalCode = postalCode,
            streetAddress = streetAddress,
            detailAddress = detailAddress
        )
    )
}
