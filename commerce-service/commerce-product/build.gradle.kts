plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(project(":commons:common-util"))
    implementation(project(":commons:model"))
    implementation(project(":commons:persistence-database"))

    runtimeOnly("com.mysql:mysql-connector-j")

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