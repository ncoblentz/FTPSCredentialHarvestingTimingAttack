plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.nickcoblentz.ftps"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-net:commons-net:3.11.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "SFTPTimingAttackClientKt"
}