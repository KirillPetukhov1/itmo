plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf(
        "-Xmx512m",
        "-XX:MaxMetaspaceSize=256m"
    )
}

tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "-Xmx512m",
        "-XX:MaxMetaspaceSize=256m"
    )
    environment("_JAVA_OPTIONS", "")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-Xmx512m", "-XX:MaxMetaspaceSize=256m")
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("fat")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())
}
