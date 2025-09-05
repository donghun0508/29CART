plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {
    api("org.springframework.kafka:spring-kafka")
    implementation(project(":modules:event-outbox"))
    implementation(project(":supports:common"))

    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:kafka")

    testFixturesImplementation("org.testcontainers:kafka")
}
