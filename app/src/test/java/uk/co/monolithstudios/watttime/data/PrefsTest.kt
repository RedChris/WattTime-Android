package uk.co.monolithstudios.watttime.data

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll

internal class PrefsTest {

    private val userMicroWaveWattage = 500

    private val context: Context = mockk(relaxed = true)
    private val sharedPreferences = mockk<SharedPreferences>()
    private val editor = mockk<SharedPreferences.Editor>()

    private val subject = Prefs(context, sharedPreferences)

    @BeforeAll
    fun setup() {
        every { sharedPreferences.edit()} returns editor
    }

    @Test
    fun getMicrowaveWattage() {
        // Arrange
        every { sharedPreferences.getInt(any(), any()) } returns userMicroWaveWattage

        // Act
        val result = subject.microwaveWattage

        // Assert
        assertEquals(userMicroWaveWattage, result, "Returned wattage is different to expected")
        verify { sharedPreferences.getInt(Prefs.PREFS_USER_MICROWAVE_WATTAGE, -1) }
    }

    @Test
    fun setMicrowaveWattage() {
        // Arrange
        every { editor.putInt(Prefs.PREFS_USER_MICROWAVE_WATTAGE, any()) } returns editor
        every { editor.apply() } returns Unit

        // Act
        subject.microwaveWattage = 700

        // Assert
        verify { editor.putInt(Prefs.PREFS_USER_MICROWAVE_WATTAGE, 700) }
    }

    @Test
    fun clearData() {
        // Arrange
        every { editor.clear() } returns editor
        every { editor.commit() } returns true

        // Act
        subject.clearData()

        // Assert
        verify { editor.clear() }
    }
}