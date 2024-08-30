package com.mock.config

import org.springframework.security.crypto.password.PasswordEncoder

class FakePasswordEncoder : PasswordEncoder {

    private val suffix = "aSDAVR14y3289fhdv"

    override fun encode(rawPassword: CharSequence?): String {
        return rawPassword!!.toString() + suffix;
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return rawPassword!!.toString() + suffix == encodedPassword
    }
}