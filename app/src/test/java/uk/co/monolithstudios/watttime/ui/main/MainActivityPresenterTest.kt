package uk.co.monolithstudios.watttime.ui.main

import android.os.Bundle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.TimeFormatter
import uk.co.monolithstudios.watttime.domain.TimerIntentLauncher
import uk.co.monolithstudios.watttime.domain.WattageCalculator

internal class MainActivityPresenterTest {

    private val view = mockk<MainView>(relaxed = true)
    private val prefs = mockk<Prefs>(relaxed = true)
    private val wattageCalculator = mockk<WattageCalculator>()
    private val timerIntentLauncher = mockk<TimerIntentLauncher>()
    private val timeFormatter = mockk<TimeFormatter>()
    private val subject = MainActivityPresenter(view, prefs, wattageCalculator, timerIntentLauncher, timeFormatter)

    @Test
    fun `init with available timer applications`() {
        // Arrange
        every { timerIntentLauncher.checkForAvailableTimerApplications() } returns true
        every { prefs.userMicrowaveWattage } returns 900

        // Act
        MainActivityPresenter(view, prefs, wattageCalculator, timerIntentLauncher, timeFormatter)

        // Assert
        verify { view.enableStartTimerButton(true) }
        verify { view.showUserMicrowaveWattage(900) }
        verify { timeFormatter.convertDurationToString(Duration.ZERO) }
        verify { view.showPackageDuration(any()) }
        verify { view.showConvertedTime(any()) }

    }

    @Test
    fun `init without available timer applications`() {
        // Arrange
        every { timerIntentLauncher.checkForAvailableTimerApplications() } returns false

        // Act
        MainActivityPresenter(view, prefs, wattageCalculator, timerIntentLauncher, timeFormatter)

        // Assert
        verify { view.enableStartTimerButton(false) }
        verify { view.showUserMicrowaveWattage(900) }
        verify { timeFormatter.convertDurationToString(Duration.ZERO) }
        verify { view.showPackageDuration(any()) }
        verify { view.showConvertedTime(any()) }

    }

    @Test
    fun onUserWantsToSetProductWattage() {
        // Arrange
        // Act
        subject.onUserWantsToSetProductWattage(800)

        // Assert
        verify { view.showPackageWattage(700) }
        verify { wattageCalculator.convertMicrowaveTime(700, any(), any()) }
        verify { timeFormatter.convertDurationToString(any()) }
        verify { view.showConvertedTime(any()) }
    }

    @Test
    fun onUserWantsToConvert() {
        // Arrange
        val duration = Duration.ofSeconds(250)

        // Act
        subject.onUserWantsToConvert(duration)

        // Assert
        verify {view.showPackageDuration(any())  }
        verify {timeFormatter.convertDurationToString(duration) }
    }

    @Test
    fun onUserWantsToChangeUserWattage() {
        // Arrange
        // Act
        subject.onUserWantsToChangeUserWattage()

        // Assert
        verify { view.showMicrowaveSettingsPage() }
    }

    @Test
    fun onUserWantsToLaunchTimer() {
        // Arrange
        // Act
        subject.onUserWantsToLaunchTimer()

        // Assert
        verify { timerIntentLauncher.startTimer(any()) }
        verify { view.showTimerSet() }

    }

    @Test
    fun saveInstance() {
        // Arrange
        val bundle = mockk<Bundle>()

        // Act
        subject.saveInstance(bundle)

        // Assert
        verify { bundle.putLong(MainActivityPresenter.KEY_MICROWAVE_SECONDS, any()) }

    }

    @Test
    fun restoreInstance() {
        // Arrange
        val bundle = mockk<Bundle>()
        every { bundle.getLong(MainActivityPresenter.KEY_MICROWAVE_SECONDS)} returns 700

        // Act
        subject.restoreInstance(bundle)

        // Assert
        verify { view.showRestoreDuration(11, 40) }
        verify { timeFormatter.convertDurationToString(Duration.ofSeconds(700)) }
        verify { view.showConvertedTime(any()) }

    }

    @Test
    fun onUserWantsToSeeSelectTimer() {
        // Arrange
        // Act
        subject.onUserWantsToSeeSelectTimer()

        // Assert
        verify { view.showUserDuration(any(), any()) }
    }

    @Test
    fun onUserWantsToViewSettings() {
        // Arrange
        // Act
        subject.onUserWantsToViewSettings()

        // Assert
        verify { view.showSettingsScreen() }

    }
}