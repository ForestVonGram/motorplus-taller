plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // PostgreSQL JDBC Driver
    implementation("org.postgresql:postgresql:42.7.4")

    // Javalin (web framework) + JSON (Jackson) + bundled plugins (CORS, etc.)
    implementation("io.javalin:javalin:5.6.1")
    implementation("io.javalin:javalin-bundle:5.6.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    // Servidor HTTP con Javalin
    mainClass.set("api.Server")
}

tasks.test {
    useJUnitPlatform()
}
