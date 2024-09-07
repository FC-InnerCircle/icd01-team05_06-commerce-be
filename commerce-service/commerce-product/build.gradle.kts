dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))

    runtimeOnly("com.mysql:mysql-connector-j")
}