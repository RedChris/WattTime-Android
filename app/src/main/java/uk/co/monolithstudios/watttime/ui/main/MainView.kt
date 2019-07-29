package uk.co.monolithstudios.watttime.ui.main


interface MainView {
    fun showUserMicrowaveWattage(userMicrowaveWattage: Int)
    fun enableStartTimerButton(showStartTimerButton: Boolean)
    fun showMicrowaveSettingsPage()
    fun showConvertedTime(durationString: String)
    fun showPackageDuration(durationString: String)
    fun showPackageWattage(wattage: Int)
    fun showRestoreDuration(minutes: Long, seconds: Long)
    fun showUserDuration(minutes: Long, seconds: Long)
}