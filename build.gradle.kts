import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.7.20"

    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

allprojects {
    group = "com.commerce"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

val nonDependenciesProjects = listOf("commons", "commerce-service")
configure(subprojects.filter { it.name !in nonDependenciesProjects }) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    // Java 컴파일러 설정
    // 빌드 이슈로 인해 Java 컴파일러 설정을 추가합니다.
    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}