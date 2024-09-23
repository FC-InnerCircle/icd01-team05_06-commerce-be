package com.commerce.service.order.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.RandomIvGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// JasyptConfig 클래스
// Jasypt 라이브러리를 사용하여 암호화를 위한 설정을 하는 클래스
// AWS 상용 데이터베이스 정보를 암호화하여 사용하기 위해 사용
@Configuration
class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        return StandardPBEStringEncryptor().apply {
            setAlgorithm("PBEWITHHMACSHA512ANDAES_256")
            setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"))
            setIvGenerator(RandomIvGenerator())
        }
    }
}