package uk.co.monolithstudios.watttime.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.ui.main.MainActivity
import uk.co.monolithstudios.watttime.ui.microwavesettings.MicrowaveSettingsActivity

class LauncherActivity : AppCompatActivity(), LauncherView {

     private lateinit var launcherPresenter: LauncherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcherPresenter = LauncherPresenter(this, Prefs(this))
    }

    override fun showMicrowaveSettingsScreen() {
        MicrowaveSettingsActivity.start(this)
        finish()
    }

    override fun showMainScreen() {
        MainActivity.start(this)
        finish()
    }
}
