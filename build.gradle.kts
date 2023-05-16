import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    `maven-publish`
}

val vv = "0.0.4"


group = "com.cool"
version = vv

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing{
    publications{
        create("maven_public",MavenPublication::class){
            groupId = "com.cool"
            artifactId = "RequestCoreDesktop"
            version = vv
            from(components.getByName("java"))
        }
    }
}
