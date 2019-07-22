package uk.co.monolithstudios.watttime.domain

import android.content.Context
import androidx.annotation.StringRes
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R

class TimeFormatter(private val context: Context) {

    fun convertDurationToSting(duration: Duration): String {
        return when {
            duration.compareTo(Duration.ofMinutes(1)) == -1
                -> context.getString(R.string.main_convertedLengthSeconds, duration.seconds)
            duration.compareTo(Duration.ofHours(1)) == -1
                -> context.getString(R.string.main_convertedLengthMinutes, duration.toMinutes(), duration.seconds)
            else
                -> context.getString(R.string.main_convertedLengthHours, duration.toHours(),
                duration.toMinutes(),
                duration.seconds)
        }
    }
}