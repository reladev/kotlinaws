/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.reladev.kotlinaws

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.server.testing.*
import kotlin.test.*

class AppTest {
    @Test
    fun testRoot(): Unit = withTestApplication(Application::mainModule) {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertEquals("Hello World!", response.content)
        }
    }
}

