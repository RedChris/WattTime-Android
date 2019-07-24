package uk.co.monolithstudios.watttime.domain

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R

class TimeFormatter(private val context: Context) {

    fun convertDurationToSting(duration: Duration): String {
        return when {
            duration.compareTo(Duration.ofMinutes(1)) == -1
            -> context.getString(R.string.main_convertedLengthSeconds,
                duration.seconds,
                getString(R.plurals.seconds, duration.seconds.toInt()))


            duration.compareTo(Duration.ofHours(1)) == -1
            -> {
                val seconds: Int = duration.minus(Duration.ofMinutes(duration.toMinutes())).seconds.toInt()
                context.getString(
                    R.string.main_convertedLengthMinutes,
                    duration.toMinutes(),
                    getString(R.plurals.minutes, duration.toMinutes().toInt()),
                    seconds,
                    getString(R.plurals.seconds,seconds)
                )
            }

            else
            -> {
                val seconds: Int = duration.minus(Duration.ofMinutes(duration.toMinutes())).seconds.toInt()
                val minutes: Int = duration.minus(Duration.ofHours(duration.toHours())).toMinutes().toInt()
                context.getString(
                    R.string.main_convertedLengthHours, duration.toHours(),
                    getString(R.plurals.hours,duration.toHours().toInt()),
                    minutes,
                    getString(R.plurals.minutes,minutes),
                    seconds,
                    getString(R.plurals.seconds,seconds))
            }
        }
    }

    private fun getString(@PluralsRes id: Int, amount: Int) = context.resources.getQuantityString(id, amount)
}