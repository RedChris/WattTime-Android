package uk.co.monolithstudios.watttime.domain

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import uk.co.monolithstudios.watttime.R

class TimerIntentLauncher(private val context: Context) {

    private val startTimerText: String = context.getString(R.string.timer_message)

    fun checkForAvailableTimerApplications(): Boolean {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
        return intent.resolveActivity(context.packageManager) != null
    }

    fun startTimer(seconds: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, startTimerText)
            putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}