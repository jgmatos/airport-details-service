plugins {
    id("java")
    id("org.springframework.boot") version "3.5.3"
    id ("io.spring.dependency-management") version "1.1.3"
}

group = "com.sporty.airport"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

val lombokVersion = "1.18.42"
val swaggerVersion = "2.2.37"
val mapstructVersion = "1.6.3"
val junit5Version = "5.13.4"
val resilienceVersion = "2.3.0"
val circuitBreakerVersion = "3.3.0"


repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/release") }
}

dependencies {

    //spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    //cache
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // Lombok
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")

    // MapStruct
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    //swagger
    implementation("io.swagger.core.v3:swagger-annotations:${swaggerVersion}")

    //resilience
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j:${circuitBreakerVersion}")
    implementation("io.github.resilience4j:resilience4j-micrometer:${resilienceVersion}")

    //Test
    testImplementation(platform("org.junit:junit-bom:${junit5Version}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
}

tasks.test {
    useJUnitPlatform()
}