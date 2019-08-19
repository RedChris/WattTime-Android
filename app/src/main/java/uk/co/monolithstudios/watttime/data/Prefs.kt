package uk.co.monolithstudios.watttime.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting

open class Prefs(context: Context) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal companion object Constants {
        const val PREFS_USER_MICROWAVE_WATTAGE = "user_microwave_wattage"
    }

    private var sharedPreferences: SharedPreferences

    init {sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)}

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    constructor(context: Context, sharedPreferences: SharedPreferences) : this(context) {
        this.sharedPreferences = sharedPreferences
    }

    var microwaveWattage: Int
        get() = sharedPreferences.getInt(PREFS_USER_MICROWAVE_WATTAGE, -1)
        set(value) = sharedPreferences.edit().putInt(PREFS_USER_MICROWAVE_WATTAGE, value).apply()

    fun clearData() {
        sharedPreferences.edit().clear().commit()
    }

}