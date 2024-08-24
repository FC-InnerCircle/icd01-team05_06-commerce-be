dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))
}