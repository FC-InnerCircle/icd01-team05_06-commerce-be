dependencies {
    implementation(project(":commons:model"))
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // Persistence
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.5.2")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.5.2")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.5.2")
}
