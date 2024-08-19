package com.commerce.admin

import com.commerce.model.TestModel
import com.commerce.persistence.TestEntity
import com.commerce.util.TestUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {

    @GetMapping("/admin/hello")
    fun hello(): String {
        val hello = """
            ${TestUtil().hello()}
            ${TestModel().hello()}
            ${TestEntity().hello()}
            hello! I'm Admin
        """.trimIndent()
        println(hello)
        return hello
    }
}