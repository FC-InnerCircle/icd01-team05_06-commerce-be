plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2" // Spring REST Docs
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0" // ktlint
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))
}

dependencies {
    description = "Commerce Admin"

    // openapi swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

val snippetsDir = layout.buildDirectory.dir("generated-snippets")

tasks {
    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(test)
        attributes(mapOf("snippets" to snippetsDir))
    }

    register<Copy>("copyRestDocs") {
        dependsOn(asciidoctor)
        from(layout.buildDirectory.dir("docs/asciidoc"))
        into(layout.buildDirectory.dir("resources/main/static/docs"))
    }

    bootJar {
        dependsOn("copyRestDocs")
    }

    jar {
        dependsOn("copyRestDocs")
    }

    named("resolveMainClassName") {
        dependsOn("copyRestDocs")
    }
}

// ktlint
ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
    // 와일드카드 임포트 규칙 비활성화
    disabledRules.set(setOf("no-wildcard-imports"))
}
