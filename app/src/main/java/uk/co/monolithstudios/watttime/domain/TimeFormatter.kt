package uk.co.monolithstudios.watttime.domain

import android.content.Context
import androidx.annotation.StringRes
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R

class TimeFormatter(private val context: Context) {

    fun convertDurationToSting(duration: Duration): String {
        return when {
            duration.compareTo(Duration.ofMinutes(1)) == -1
                -> resolveString(R.string.main_convertedLengthSeconds, duration.seconds.toInt())
            duration.compareTo(Duration.ofHours(1)) == -1
                -> resolveString(R.string.main_convertedLengthMinutes, duration.toMinutes().toInt(), duration.seconds.toInt())
            else
                -> resolveString(R.string.main_convertedLengthMinutes, duration.toHours().toInt(),
                duration.toMinutes().toInt(),
                duration.seconds.toInt())
        }

    }

    private fun resolveString(@StringRes stringId: Int, vararg strings: Int)
            = context.getString(stringId).format(strings)

}