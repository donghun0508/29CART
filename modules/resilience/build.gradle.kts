plugins {
    `java-library`
}

dependencies {
    // resilience4j
    api("io.github.resilience4j:resilience4j-spring-boot3")
    api("org.springframework.boot:spring-boot-starter-aop")
}
