package uk.co.monolithstudios.watttime.ui.microwavesettings

import uk.co.monolithstudios.watttime.data.Prefs

class MicrowaveSettingsPresenter (private val microwaveSettingsView: MicrowaveSettingsView, private val prefs: Prefs) {

    private var userWattage = 0

    init {
        microwaveSettingsView.setShowSaveButton(false)

        if (prefs.userMicrowaveWattage == -1) {
            microwaveSettingsView.shoWattage(800)
        } else {
            microwaveSettingsView.shoWattage(prefs.userMicrowaveWattage)
        }
    }

    fun onUserSelectedWattage(wattage: Int) {
        userWattage = wattage
        microwaveSettingsView.setShowSaveButton(true)
    }

    fun onUserWantsToSaveWattage() {
        prefs.userMicrowaveWattage = userWattage
        microwaveSettingsView.showMainScreen()
    }
}