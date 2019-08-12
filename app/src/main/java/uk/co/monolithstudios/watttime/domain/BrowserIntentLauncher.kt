package uk.co.monolithstudios.watttime.domain

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import uk.co.monolithstudios.watttime.BuildConfig

class BrowserIntentLauncher(private val context: Context) {

    public fun launchAppStorePage() {
        val appId = BuildConfig.APPLICATION_ID
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")))
        } catch (exception: ActivityNotFoundException) {
            exception.printStackTrace()
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appId")
                )
            )
        }
    }
}

