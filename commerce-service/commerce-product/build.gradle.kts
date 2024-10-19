plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.palantir.docker") version "0.35.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    compileOnly("org.springframework:spring-tx")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))
    implementation(project(":commons:common-web"))
    implementation(project(":commons:common-jwt"))
    testImplementation(project(":commons:test-helper"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

val asciidoctorExt: Configuration by configurations.creating
dependencies {
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks {
    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        baseDirFollowsSourceFile()
        dependsOn(test)
        doLast {
            copy {
                from("build/docs/asciidoc")
                into("build/resources/main/static/docs")
            }
        }
    }

    bootJar {
        dependsOn(asciidoctor)
    }
}

docker {
    val dockerImageName = project.findProperty("dockerImageName") as String?
    val profile = project.findProperty("profile") as String?

    name = dockerImageName ?: "${project.name}:${version}"
    setDockerfile(file(if (profile == "local") "../../Dockerfile-local" else "../../Dockerfile"))
    files("../../INNER-CIRCLE.pem")
    files(tasks.bootJar.get().outputs.files)
    buildArgs(mapOf(
        "JAR_FILE" to tasks.bootJar.get().outputs.files.singleFile.name
    ))
}