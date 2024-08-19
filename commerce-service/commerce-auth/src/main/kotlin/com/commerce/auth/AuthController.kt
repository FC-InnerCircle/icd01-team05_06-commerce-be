package com.commerce.auth

import com.commerce.model.TestModel
import com.commerce.persistence.TestEntity
import com.commerce.util.TestUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @GetMapping("/auth/hello")
    fun hello(): String {
        val hello = """
            ${TestUtil().hello()}
            ${TestModel().hello()}
            ${TestEntity().hello()}
            hello! I'm Auth
        """.trimIndent()
        println(hello)
        return hello
    }
}