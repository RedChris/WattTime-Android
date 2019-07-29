package uk.co.monolithstudios.watttime.ui.main

import android.os.Bundle
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.TimeFormatter
import uk.co.monolithstudios.watttime.domain.TimerIntentLauncher
import uk.co.monolithstudios.watttime.domain.WattageCalculator

class MainActivityPresenter (private val mainView: MainView,
                             prefs: Prefs,
                             private val wattageCalculator: WattageCalculator,
                             private val timerIntentLauncher: TimerIntentLauncher,
                             private val timeFormatter: TimeFormatter) {

    private val KEY_MICROWAVE_SECONDS = "KEY_MICROWAVE_SECONDS"

    private val userMicrowaveWattage = prefs.microwaveWattage
    private val hasAvailableTimerIntentApplications = timerIntentLauncher.checkForAvailableTimerApplications()
    private var wattage = 0
    private var duration: Duration = Duration.ZERO
    private var convertedDuration: Duration = Duration.ZERO


    init {
        if (!hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(false)
        }

        mainView.showUserMicrowaveWattage(userMicrowaveWattage)
        mainView.showPackageDuration(timeFormatter.convertDurationToSting(duration))
        mainView.showPackageWattage(wattage)
        convertWattage()
    }

    fun onUserWantsToSetProductWattage(productWattage: Int) {
        this.wattage = productWattage
        mainView.showPackageWattage(wattage)
        convertWattage()
    }

    fun onUserWantsToConvert(duration: Duration) {
        this.duration = duration
        mainView.showPackageDuration(timeFormatter.convertDurationToSting(duration))
        convertWattage()
    }

    private fun convertWattage() {
        val convertedDuration = wattageCalculator.convertMicrowaveTime(userMicrowaveWattage, wattage, duration)
        this.convertedDuration = convertedDuration
        mainView.showConvertedTime(timeFormatter.convertDurationToSting(convertedDuration))
        if (hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(true)
        }
    }

    fun onUserWantsToChangeUserWattage() {
        mainView.showMicrowaveSettingsPage()
    }

    fun onUserWantsToLaunchTimer() {
        timerIntentLauncher.startTimer(convertedDuration.seconds.toInt())
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
}