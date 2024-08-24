dependencies {
    implementation(project(":commons:model"))
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // Persistence
    runtimeOnly("com.h2database:h2")
}
