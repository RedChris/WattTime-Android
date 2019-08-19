package uk.co.monolithstudios.watttime.domain

import android.content.Context
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll

internal class BrowserIntentLauncherTest {

    private val context: Context = mockk(relaxed = true)

    private val subject = BrowserIntentLauncher(context)

    @BeforeAll
    fun setup() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns Uri.EMPTY
    }

    @Test
    fun launchAppStorePage() {
        // Arrange
        every { context.startActivity(any()) } returns Unit

        // Act
        subject.launchAppStorePage()

        // Assert
        verify { context.startActivity(any()) }
    }
}