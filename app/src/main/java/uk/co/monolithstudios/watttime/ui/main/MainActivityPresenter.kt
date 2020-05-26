package uk.co.monolithstudios.watttime.ui.main

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.TimeFormatter
import uk.co.monolithstudios.watttime.domain.TimerIntentLauncher
import uk.co.monolithstudios.watttime.domain.WattageCalculator

class MainActivityPresenter (private val mainView: MainView,
                             private val prefs: Prefs,
                             private val wattageCalculator: WattageCalculator,
                             private val timerIntentLauncher: TimerIntentLauncher,
                             private val timeFormatter: TimeFormatter) {

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        internal val KEY_MICROWAVE_SECONDS = "KEY_MICROWAVE_SECONDS"

    }

    private val userMicrowaveWattage = prefs.userMicrowaveWattage
    private val hasAvailableTimerIntentApplications = timerIntentLauncher.checkForAvailableTimerApplications()
    private var wattage = 0
    private var duration: Duration = Duration.ZERO
    private var convertedDuration: Duration = Duration.ZERO


    init {
        if (!hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(false)
        }

        mainView.showUserMicrowaveWattage(userMicrowaveWattage)
        mainView.showPackageDuration(timeFormatter.convertDurationToString(duration))
        mainView.showPackageWattage(wattage)
        if (prefs.packageMicrowaveWattage != -1) {
            mainView.showPreviousProductWattage(prefs.packageMicrowaveWattage)
        }
        convertWattage()
    }

    fun onUserWantsToSetProductWattage(productWattage: Int) {
        prefs.packageMicrowaveWattage = productWattage
        this.wattage = productWattage
        mainView.showPackageWattage(wattage)
        convertWattage()
    }

    fun onUserWantsToConvert(duration: Duration) {
        this.duration = duration
        mainView.showPackageDuration(timeFormatter.convertDurationToString(duration))
        convertWattage()
    }

    private fun convertWattage() {
        val convertedDuration = wattageCalculator.convertMicrowaveTime(userMicrowaveWattage, wattage, duration)
        this.convertedDuration = convertedDuration
        mainView.showConvertedTime(timeFormatter.convertDurationToString(convertedDuration))
        if (hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(true)
        }
    }

    fun onUserWantsToChangeUserWattage() {
        mainView.showMicrowaveSettingsPage()
    }

    fun onUserWantsToLaunchTimer() {
        timerIntentLauncher.startTimer(convertedDuration.seconds.toInt())
        mainView.showTimerSet()
    }

    fun saveInstance(outState: Bundle) {
        outState.putLong(KEY_MICROWAVE_SECONDS, duration.toMillis())
    }

    fun restoreInstance(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restoredUserDuration = Duration.ofMillis(savedInstanceState.getLong(KEY_MICROWAVE_SECONDS))
            val seconds = restoredUserDuration.minus(Duration.ofMinutes(restoredUserDuration.toMinutes())).seconds
            mainView.showRestoreDuration(restoredUserDuration.toMinutes(), seconds)
            onUserWantsToConvert(restoredUserDuration)
        }
    }

    fun onUserWantsToSeeSelectTimer() {
        val seconds = duration.minus(Duration.ofMinutes(duration.toMinutes())).seconds
        mainView.showUserDuration(duration.toMinutes(), seconds)
    }

    fun onUserWantsToViewSettings() {
        mainView.showSettingsScreen()
    }
}