package com.commerce.admin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class DocsController {
    @GetMapping("/docs")
    fun getDocs(): String = "redirect:/docs/index.html"
}
