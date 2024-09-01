import groovy.xml.dom.DOMCategory.attributes

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
