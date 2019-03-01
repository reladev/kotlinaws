import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.cli.common.arguments.parseCommandLineArguments
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.*
import java.time.*
import java.time.format.*

val project_version = "1.3"
val date = LocalDateTime.now()

val ktor_version = "1.1.2"
val logback_version = "1.2.3"
val slf4j_version = "1.7.25"

plugins {
    application
    id("org.jetbrains.kotlin.jvm").version("1.3.20")
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "org.reladev.kotlinaws.AppKt"
}

tasks.withType<ShadowJar>  {
    archiveBaseName.set("${project.name}-all")
}

tasks.jar {
    doLast{
        val gitHash = execOutput {
            commandLine("git", "rev-parse", "--short", "HEAD")
        }
        val format = DateTimeFormatter.ofPattern("YYMMddHHmm")
        File("build/libs/version.txt").writeText("""
                       version:$project_version
                       date: $date
                       git: $gitHash
                       tag: ${project_version}_${date.format(format)}
                   """.trimIndent())
    }
}

fun execOutput(block: ExecSpec.() -> Unit): String {
    val output = ByteArrayOutputStream()
    exec {
        standardOutput = output
        block()
    }
    return output.toString().trim()
}