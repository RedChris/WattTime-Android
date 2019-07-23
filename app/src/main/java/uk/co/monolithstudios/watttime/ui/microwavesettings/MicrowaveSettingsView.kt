package uk.co.monolithstudios.watttime.ui.microwavesettings

interface MicrowaveSettingsView {
    fun shoWattage(wattage: Int)
    fun setShowSaveButton(showSaveButton: Boolean)
    fun showMainScreen()
}