package com.common

import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Attributes.key

class RestDocsUtil {
    companion object {
        fun format(value: String): Attributes.Attribute {
            return key("format").value(value)
        }
    }
}