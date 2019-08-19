package uk.co.monolithstudios.watttime.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.threeten.bp.Duration

internal class WattageCalculatorTest {

    private val subject = WattageCalculator()

    @Test
    fun `convertMicrowaveTime  scaling up`() {
        // Arrange
        // Act
        val result = subject.convertMicrowaveTime(10, 100, Duration.ofMinutes(5))

        // Assert
        assertEquals(Duration.ofSeconds(3000), result)
    }

    @Test
    fun `convertMicrowaveTime  scaling down`() {
        // Arrange
        // Act
        val result = subject.convertMicrowaveTime(100, 10, Duration.ofMinutes(5))

        // Assert
        assertEquals(Duration.ofSeconds(30), result)
    }

    @Test
    fun `convertMicrowaveTime  long duration`() {
        // Arrange
        // Act
        val result = subject.convertMicrowaveTime(100, 200, Duration.ofMinutes(100))

        // Assert
        assertEquals(Duration.ofSeconds(12000), result)
    }
}