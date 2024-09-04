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