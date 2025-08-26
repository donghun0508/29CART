plugins {
    `java-library`
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    // feign
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("com.squareup.okhttp3:okhttp")
    api("com.squareup.okhttp3:logging-interceptor")
}
