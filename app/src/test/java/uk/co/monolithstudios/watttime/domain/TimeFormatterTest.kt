package uk.co.monolithstudios.watttime.domain

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import io.mockk.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R

internal class TimeFormatterTest {

    private val context: Context = mockk(relaxed = true)
    private val resources: Resources = mockk(relaxed = true)

    private val subject = TimeFormatter(context)

    @BeforeAll
    fun setup() {
        every { context.resources  } returns  resources

        mockkStatic(Uri::class)
        every { Uri.parse("http://test/path") } returns Uri.EMPTY
        every { resources.getQuantityString(any(), any()) } returns ""
    }

    @Test
    fun `convert duration under 1 minute to string `() {
        // Arrange
        val duration: Duration = Duration.ofSeconds(59)

        // Act
        subject.convertDurationToString(duration)

        // Assert
        verify { resources.getQuantityString(R.plurals.seconds, 59) }
    }

    @Test
    fun `convert duration under 1 hour to String `() {
        // Arrange
        val duration: Duration = Duration.ofMinutes(59)

        // Act
        subject.convertDurationToString(duration)

        // Assert
        verify { resources.getQuantityString(R.plurals.seconds, 0) }
        verify { resources.getQuantityString(R.plurals.minutes, 59) }
    }

    @Test
    fun `convert duration over 1 hour to String `() {
        // Arrange
        val duration: Duration = Duration.ofHours(1)

        // Act
        subject.convertDurationToString(duration)

        // Assert
        verify { resources.getQuantityString(R.plurals.seconds, 0) }
        verify { resources.getQuantityString(R.plurals.minutes, 0) }
        verify { resources.getQuantityString(R.plurals.hours, 1) }
    }
}