package uk.co.monolithstudios.watttime.ui.settings

interface SettingsView {
    fun setAppVersion(versionName: String)
    fun showUserEntryScreen()
    fun showPrivacyPolicy()
}