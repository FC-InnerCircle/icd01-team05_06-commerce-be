package com.commerce.common.persistence

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EntityScan("com.commerce.common.persistence")
@EnableJpaRepositories("com.commerce.common.persistence")
@Configuration
class PersistenceConfiguration
