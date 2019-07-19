package uk.co.monolithstudios.watttime.data

import android.content.Context
import android.content.SharedPreferences

open class Prefs(context: Context) {

    private companion object Constants {
        const val PREFS_USER_MICROWAVE_WATTAGE = "user_microwave_wattage"
    }

    private val sharedPreferences: SharedPreferences
            = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    var microwaveWattage: Int
        get() = sharedPreferences.getInt(PREFS_USER_MICROWAVE_WATTAGE, -1)
        set(value) = sharedPreferences.edit().putInt(PREFS_USER_MICROWAVE_WATTAGE, value).apply()

}