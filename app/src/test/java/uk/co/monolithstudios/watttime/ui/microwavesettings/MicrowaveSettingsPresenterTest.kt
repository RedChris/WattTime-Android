package uk.co.monolithstudios.watttime.ui.microwavesettings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import uk.co.monolithstudios.watttime.data.Prefs

internal class MicrowaveSettingsPresenterTest {

    private val view = mockk<MicrowaveSettingsView>(relaxed = true)
    private val prefs = mockk<Prefs>(relaxed = true)
    private val subject = MicrowaveSettingsPresenter(view, prefs)

    @Test
    fun `init from initial load`() {
        // Arrange
        every { prefs.userMicrowaveWattage} returns -1

        // Act
        MicrowaveSettingsPresenter(view, prefs)

        // Assert
        verify { view.setShowSaveButton(false) }
        verify { view.shoWattage(800) }

    }


    @Test
    fun `init from after initial load`() {
        // Arrange
        every { prefs.userMicrowaveWattage} returns 500

        // Act
        MicrowaveSettingsPresenter(view, prefs)

        // Assert
        verify { view.setShowSaveButton(false) }
        verify { view.shoWattage(500) }

    }

    @Test
    fun onUserSelectedWattage() {
        // Arrange
        // Act
        subject.onUserSelectedWattage(400)

        // Assert
        verify { view.setShowSaveButton(true) }
    }

    @Test
    fun onUserWantsToSaveWattage() {
        // Arrange

        // Act
        subject.onUserSelectedWattage(300)
        subject.onUserWantsToSaveWattage()

        // Assert
        verify { view.showMainScreen() }
        verify { prefs.userMicrowaveWattage = 300 }
    }
}