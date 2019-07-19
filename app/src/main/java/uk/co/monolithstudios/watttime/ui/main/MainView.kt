package uk.co.monolithstudios.watttime.ui.main

interface MainView {
    fun showUserMicrowaveWattage(userMicrowaveWattage: Int)
    fun enableStartTimerButton(showStartTimerButton: Boolean)
    fun showMicrowaveSettingsPage()
    fun showConvertedTime(durationString: String)
}