plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2" // Spring REST Docs
    id("com.palantir.docker") version "0.35.0"
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
    implementation(project(":commons:common-web"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // 접속 정보 암호화
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    runtimeOnly("com.mysql:mysql-connector-j") // MySQL
}

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

tasks {
    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        dependsOn(test)
        doFirst { // 작업이 실행되기전 수행하는 작업 선언
            delete("build/resources/main/static/docs") // 기존 문서 삭제
        }
        baseDirFollowsSourceFile()
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

    name = dockerImageName ?: "${project.name}:${version}"
    setDockerfile(file("../../Dockerfile"))
    files(tasks.bootJar.get().outputs.files)
    buildArgs(mapOf(
        "JAR_FILE" to tasks.bootJar.get().outputs.files.singleFile.name
    ))
}