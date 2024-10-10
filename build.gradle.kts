import com.google.protobuf.gradle.id

plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.protobuf") version "0.9.4"
    id("org.sonarqube") version "4.0.0.2929"
    jacoco
}

group = "ru.customer.petprojects"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val grpcStarterVersion = "2.15.0.RELEASE"
val grpcVersion = "1.58.0"
val protobufVersion = "3.25.1"

dependencies {
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.1")
    implementation("redis.clients:jedis:5.1.0")

    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.2.2")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin:3.1.10")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("io.grpc:grpc-netty:${grpcVersion}")
    implementation("net.devh:grpc-spring-boot-starter:$grpcStarterVersion") {
        exclude(group = "io.grpc", module = "grpc-netty-shaded")
    }
    implementation("io.grpc:grpc-netty")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        compileOnly("org.apache.tomcat:annotations-api:6.0.53")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    }

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.liquibase:liquibase-core")
    implementation("org.modelmapper:modelmapper:3.2.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.grpc:grpc-testing:1.58.0")
    testImplementation("com.redis.testcontainers:testcontainers-redis-junit:1.6.4")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc") { }
            }
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectName", "customer-service")
        property("sonar.projectKey", "customer-service")
        property("sonar.host.url", "*URL*")
        property("sonar.qualitygate.wait", true)
        property(
            "sonar.coverage.exclusions",
            "customerservice," +
                    "**/dto/**, " +
                    "**/advice/**, " +
                    "**/configuration/**, " +
                    "**/mapper/**, " +
                    "**/model/**, " +
                    "**/repository/**, " +
                    "**/proto/**"
        )
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://nexus.*URL*/repository/zinovievBank-maven/")
        credentials {
            username = System.getenv("USER_NEXUS_*URL*") ?: "*LOGIN*"
            password = System.getenv("PASS_NEXUS_*URL*") ?: "*PASS*"
        }
    }
    flatDir {
        dirs("libs")
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

java.sourceSets["main"].java {
    srcDir("${buildDir}/generated/src/main/java")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.named("sonarqube").configure {
    dependsOn(tasks.test)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.84".toBigDecimal()
            }
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.84".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.62".toBigDecimal()
            }
            limit {
                counter = "COMPLEXITY"
                value = "COVEREDRATIO"
                minimum = "0.50".toBigDecimal()
            }
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.84".toBigDecimal()
            }
            limit {
                counter = "CLASS"
                value = "MISSEDCOUNT"
                maximum = "2".toBigDecimal()
            }
        }
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "customerservice",
                        "**/configuration/**",
                        "**/dto/**",
                        "**/advice/**",
                        "**/mapper/**",
                        "**/model/**",
                        "**/repository/**",
                        "**/proto/**",
                        "**/kafka/**",
                        "**/grpc/**"
                    )
                }
            })
        )
    }
}
