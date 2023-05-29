import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    id("org.graalvm.buildtools.native") version "0.9.22"
    id( "com.github.ben-manes.versions") version "0.46.0"
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation( "org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("junit")
    }
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql:42.6.0")
    implementation("org.flywaydb:flyway-core")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    implementation("com.auth0:java-jwt:4.4.0")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.3.0")
    testImplementation("io.zonky.test:embedded-postgres:2.0.4")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage>{
    imageName.set("ghcr.io/kris456/mealweek-backend")
}


extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}
version = "0.1.0"
group= "no.krismobchr"
