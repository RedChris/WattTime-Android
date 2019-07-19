package uk.co.monolithstudios.watttime.ui.microwavesettings

import uk.co.monolithstudios.watttime.data.Prefs

class MicrowaveSettingsPresenter (private val microwaveSettingsView: MicrowaveSettingsView, private val prefs: Prefs) {

    var userWattage = 0

    init {
        microwaveSettingsView.setShowSaveButton(false)
    }

    fun onUserSelectedWattage(wattage: Int) {
        userWattage = wattage
        microwaveSettingsView.setShowSaveButton(true)
    }

    fun onUserWantsToSaveWattage() {
        prefs.microwaveWattage = userWattage
        microwaveSettingsView.showMainScreen()
    }
}