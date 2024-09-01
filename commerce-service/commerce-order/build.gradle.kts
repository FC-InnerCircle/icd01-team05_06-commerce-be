plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2" // Spring REST Docs
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

val snippetsDir = layout.buildDirectory.dir("generated-snippets")
val outputDir = layout.buildDirectory.dir("asciidoc/html5")

tasks {
    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        setOutputDir(layout.buildDirectory.dir("asciidoc/html5").get().asFile)
        dependsOn(test)
        finalizedBy("copyAsciidoctorDocs")
    }

    register<Copy>("copyAsciidoctorDocs") {
        from(outputDir)
        into(layout.projectDirectory.dir("src/main/resources/static/docs"))
    }

    bootJar {
        dependsOn(asciidoctor)
        from(outputDir) {
            into("static/docs")
        }
    }
}

tasks.named("build") {
    dependsOn("copyAsciidoctorDocs")
}