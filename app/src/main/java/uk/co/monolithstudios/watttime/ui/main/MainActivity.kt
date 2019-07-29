package uk.co.monolithstudios.watttime.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.TimeFormatter
import uk.co.monolithstudios.watttime.domain.TimerIntentLauncher
import uk.co.monolithstudios.watttime.domain.WattageCalculator
import uk.co.monolithstudios.watttime.ui.common.views.SliderLayoutManager
import uk.co.monolithstudios.watttime.ui.microwavesettings.MicrowaveSettingsActivity
import android.view.WindowManager
import android.os.Build
import android.content.res.Configuration
import uk.co.monolithstudios.watttime.Constants


class MainActivity : AppCompatActivity(), MainView, TimePickerFragment.OnFragmentInteractionListener {

    lateinit var mainActivityPresenter: MainActivityPresenter
    private var timeNumberPickerFragment: TimePickerFragment? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        setContentView(R.layout.activity_main)

        val animationBackground = mainLayout.background as AnimationDrawable
        animationBackground.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(2000)
            start()
        }

        mainActivityPresenter = MainActivityPresenter(this, Prefs(this), WattageCalculator(), TimerIntentLauncher(this), TimeFormatter(this))

        numberPicker.setOnItemSelectedListener(object : SliderLayoutManager.OnItemSelectedListener {
            override fun onItemSelected(layoutPosition: Int) {
                mainActivityPresenter.onUserWantsToSetProductWattage(Constants.wattages[layoutPosition])
            }
        })

        numberPicker.setData(Constants.wattages.map { it.toString() })

        timerButton.setOnClickListener { mainActivityPresenter.onUserWantsToLaunchTimer() }

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            timeNumberPickerFragment = TimePickerFragment.newInstance(0, 0, false)
            supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, timeNumberPickerFragment!!).commit()
        } else {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentHolder)
            if (fragment != null) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityPresenter.saveInstance(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mainActivityPresenter.restoreInstance(savedInstanceState)
    }

    override fun showRestoreDuration(minutes: Long, seconds: Long) {
        timeNumberPickerFragment?.setTime(minutes, seconds)
    }

    override fun showUserDuration(minutes: Long, seconds: Long) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val timeNumberPickerFragment = TimePickerFragment.newInstance(minutes.toInt(), seconds.toInt(), true)
            timeNumberPickerFragment.show(supportFragmentManager, "timeNumberPickerFragment")
        } else {
            timeNumberPickerFragment?.setTime(minutes, seconds)
        }
    }

    override fun showPackageWattage(wattage: Int) {
        foodPackageText.text = getString(R.string.main_foodPackageText).format(wattage)
    }

    override fun showPackageDuration(durationString: String) {
        forTimeText.text = getString(R.string.main_for, durationString)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val spannable = SpannableString(getString(R.string.main_for, durationString))

            spannable.setSpan(object: ClickableSpan() {
                override fun onClick(widget: View) {
                    mainActivityPresenter.onUserWantsToSeeSelectTimer()
                }

            } , 4, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(1.2f),  4, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            forTimeText.apply {
                text = spannable
                movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    override fun onTimeChanged(seconds: Int, minutes: Int) {
        val duration = Duration.ofMinutes(minutes.toLong()).plusSeconds(seconds.toLong())
        mainActivityPresenter.onUserWantsToConvert(duration)
    }

    override fun showUserMicrowaveWattage(userMicrowaveWattage: Int) {
        val spannable = SpannableString(getString(R.string.main_yourMicrowaveText, userMicrowaveWattage.toString()))

        spannable.setSpan(object: ClickableSpan() {
            override fun onClick(widget: View) {
                mainActivityPresenter.onUserWantsToChangeUserWattage()
            }

        } , 20, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(1.2f),  21, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        yourMicrowaveText.apply {
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun enableStartTimerButton(showStartTimerButton: Boolean) {
        timerButton.visibility = if (showStartTimerButton) View.VISIBLE else View.INVISIBLE
    }

    override fun showMicrowaveSettingsPage() {
        MicrowaveSettingsActivity.start(this)
    }

    override fun showConvertedTime(durationString: String) {
        resultText.text = getString(R.string.main_resultText, durationString)
    }
}
