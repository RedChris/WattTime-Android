package uk.co.monolithstudios.watttime

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.Crashlytics

class WattTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Fabric.with(this, Crashlytics())
    }
}