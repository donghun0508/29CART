dependencies {
//    implementation(project(":modules:kafka"))
    implementation(project(":modules:jpa"))
    implementation(project(":supports:common"))

    implementation("io.micrometer:micrometer-tracing-bridge-brave")

    // jdbc-mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.testcontainers:mysql")
}
