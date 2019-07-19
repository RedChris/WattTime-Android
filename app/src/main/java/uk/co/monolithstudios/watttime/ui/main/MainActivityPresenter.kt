package uk.co.monolithstudios.watttime.ui.main

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

    val userMicrowaveWattage = prefs.microwaveWattage
    val hasAvailableTimerIntentApplications = timerIntentLauncher.checkForAvailableTimerApplications()
    var duration: Duration? = null


    init {
        if (!hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(false)
        }

        mainView.showUserMicrowaveWattage(userMicrowaveWattage)
    }

    fun onUserWantsToConvert(duration: Duration, prodcutWattage: Int) {
        val convertedDuration = wattageCalculator.convertMicrowaveTime(userMicrowaveWattage, prodcutWattage, duration)
        mainView.showConvertedTime(timeFormatter.convertDurationToSting(convertedDuration))
        if (hasAvailableTimerIntentApplications) {
            mainView.enableStartTimerButton(true)
        }
    }

    fun onUserWantsToChangeUserWattage() {
        mainView.showMicrowaveSettingsPage()
    }

    fun onUserWantsToLauncherTimer() {
        duration?.let { timerIntentLauncher.startTimer(it.seconds.toInt()) }

    }
}