package uk.co.monolithstudios.watttime.ui.launcher

import uk.co.monolithstudios.watttime.data.Prefs

class LauncherPresenter(launcherView: LauncherView, prefs: Prefs) {

    init {
        if (prefs.microwaveWattage == -1) {
            launcherView.showMicrowaveSettingsScreen()
        } else {
            launcherView.showMainScreen()
        }
    }
}