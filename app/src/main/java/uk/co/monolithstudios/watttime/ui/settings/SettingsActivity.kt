package uk.co.monolithstudios.watttime.ui.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.BrowserIntentLauncher
import android.webkit.WebView
import android.webkit.WebViewClient
import uk.co.monolithstudios.watttime.domain.Constants
import uk.co.monolithstudios.watttime.ui.microwavesettings.MicrowaveSettingsActivity


class SettingsActivity : AppCompatActivity(), SettingsView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    lateinit var settingsPresenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val animationBackground = mainLayout.background as AnimationDrawable
        animationBackground.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(2000)
            start()
        }

        settingsPresenter = SettingsPresenter(this, Prefs(this), BrowserIntentLauncher(this))

        clearData.setOnClickListener { showClearDataDialog() }

        leaveFeedback.setOnClickListener { settingsPresenter.onUserWantsToLeaveFeedback() }

        privacyPolicy.setOnClickListener { settingsPresenter.onUserWantsToViewPrivacyPolicy() }
    }

    override fun showPrivacyPolicy() {
        val builder = AlertDialog.Builder(this, R.style.MyAlertDialogTheme)

        with(builder)
        {

            val wv = WebView(this@SettingsActivity)
            wv.loadUrl(Constants.PRIVACY_POLICY_URL)
            wv.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }

            builder.setTitle(getString(R.string.settings_privacyPolicyDialog))
            builder.setView(wv)
            builder.setNegativeButton(getString(R.string.settings_dialogClose), dismissButtonClick )
            builder.show()
        }
    }

    override fun showUserEntryScreen() {
        finish()
        MicrowaveSettingsActivity.start(this)
    }

    override fun setAppVersion(versionName: String) {
        appVersion.text = getString(R.string.settings_appVersion, versionName)
    }

    private fun showClearDataDialog() {
        val builder = AlertDialog.Builder(this, R.style.MyAlertDialogTheme)

        with(builder)
        {
            setTitle(getString(R.string.settings_clearDataDialog))
            setMessage(getString(R.string.settings_areYouSure))
            setPositiveButton(getString(R.string.settings_clear), clearDataButtonClick)
            setNegativeButton(getString(R.string.settings_Cancel), dismissButtonClick)
            show()
        }
    }

    private val clearDataButtonClick = { dialog: DialogInterface, which: Int ->
        settingsPresenter.onUserWantsToClearData()
    }

    private val dismissButtonClick = { dialog: DialogInterface, which: Int ->
        dialog.dismiss()
    }
}