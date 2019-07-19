package uk.co.monolithstudios.watttime.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.monolithstudios.watttime.R

class MainActivity : AppCompatActivity(), MainView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showUserMicrowaveWattage(userMicrowaveWattage: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableStartTimerButton(showStartTimerButton: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMicrowaveSettingsPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showConvertedTime(durationString: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
