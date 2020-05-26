package uk.co.monolithstudios.watttime.ui.launcher

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import uk.co.monolithstudios.watttime.data.Prefs

internal class LauncherPresenterTest {

private val view = mockk<LauncherView>(relaxed = true)
private val prefs = mockk<Prefs>()

    @Test
    fun `init on first load`() {
        // Arrange
        every { prefs.userMicrowaveWattage } returns -1

        // Act
        LauncherPresenter(view, prefs)

        // Assert
        verify { view.showMicrowaveSettingsScreen() }

    }

    @Test
    fun `init after first load`() {
        // Arrange
        every { prefs.userMicrowaveWattage } returns 0

        // Act
        LauncherPresenter(view, prefs)

        //Assert
        verify { view.showMainScreen() }
    }

}