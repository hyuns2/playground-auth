plugins {
    `java-library`
    `maven-publish`
}

group = "io.playground"
version = "0.0.1-SNAPSHOT"
description = "security-core"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = project.description
            version = project.version.toString()
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(
                "https://maven.pkg.github.com/${System.getenv("GITHUB_OWNER")}/${System.getenv("GITHUB_REPOSITORY")}"
            )
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.1.0"))

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework.boot:spring-boot-starter-security")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    api("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
