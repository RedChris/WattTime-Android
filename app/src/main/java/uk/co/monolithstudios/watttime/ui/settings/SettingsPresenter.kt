package uk.co.monolithstudios.watttime.ui.settings

import uk.co.monolithstudios.watttime.BuildConfig
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.BrowserIntentLauncher

class SettingsPresenter (private val settingsView: SettingsView, private val prefs: Prefs, private val browserIntentLauncher: BrowserIntentLauncher) {

    init {
        settingsView.setAppVersion(BuildConfig.VERSION_NAME)
    }

    fun onUserWantsToLeaveFeedback() {
        browserIntentLauncher.launchAppStorePage()
    }

    fun onUserWantsToViewPrivacyPolicy() {
        settingsView.showPrivacyPolicy()
    }

    fun onUserWantsToClearData() {
        prefs.clearData()
        settingsView.showUserEntryScreen()
    }


}