package uk.co.monolithstudios.watttime.ui.settings

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import uk.co.monolithstudios.watttime.BuildConfig
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.BrowserIntentLauncher

internal class SettingsPresenterTest {


    private val view = mockk<SettingsView>(relaxed = true)
    private val prefs = mockk<Prefs>(relaxed = true)
    private val browserIntentLauncher = mockk<BrowserIntentLauncher>(relaxed = true)
    private val subject = SettingsPresenter(view, prefs, browserIntentLauncher)

    @Test
    fun init() {
        // Arrange
        // Act
        SettingsPresenter(view, prefs, browserIntentLauncher)

        // Assert
        verify { view.setAppVersion(BuildConfig.VERSION_NAME) }
    }

    @Test
    fun onUserWantsToLeaveFeedback() {
        // Arrange
        // Act
        subject.onUserWantsToLeaveFeedback()

        // Assert
        verify { browserIntentLauncher.launchAppStorePage() }
    }

    @Test
    fun onUserWantsToViewPrivacyPolicy() {
        // Arrange
        // Act
        subject.onUserWantsToViewPrivacyPolicy()

        // Assert
        verify {view.showPrivacyPolicy() }
    }

    @Test
    fun onUserWantsToClearData() {
        // Arrange
        // Act
        subject.onUserWantsToClearData()

        // Assert
        verify {prefs.clearData() }
        verify {view.showUserEntryScreen() }
    }
}