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
import android.graphics.Color
import android.os.Parcelable
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.mainLayout
import kotlinx.android.synthetic.main.activity_main.numberPicker
import kotlinx.android.synthetic.main.activity_microwave_settings.*
import uk.co.monolithstudios.watttime.domain.Constants
import uk.co.monolithstudios.watttime.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), MainView, TimePickerFragment.OnFragmentInteractionListener {

    private val KEY_LAYOUT_MANAGER_STATE = "keylayoutmanagerstate"

    lateinit var mainActivityPresenter: MainActivityPresenter
    private var timeNumberPickerFragment: TimePickerFragment? = null

    private var mLayoutManagerState: Parcelable? = null
    private var defaultPosition = 0

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {

            if (Build.VERSION.SDK_INT in 19..20) {
                        addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                    } else {
                        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            decorView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        } else {
                            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            statusBarColor = Color.TRANSPARENT
                    navigationBarColor = Color.TRANSPARENT
                }
            }
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
        if (savedInstanceState != null) {
            val layoutManagerState: Parcelable = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER_STATE)!!
            numberPicker.restoreInstance(layoutManagerState)
        } else {
            numberPicker.goToPosition(defaultPosition)
        }

        settingsButton.setOnClickListener { mainActivityPresenter.onUserWantsToViewSettings() }

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

    override fun onPause() {
        super.onPause()
        mLayoutManagerState = numberPicker.saveInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityPresenter.saveInstance(outState)
        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, mLayoutManagerState)
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
        try {
            val debugText = forTimeText.text
            Crashlytics.log("is landscape = " + (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE))
            forTimeText.text = getString(R.string.main_for, durationString)

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val spannable = SpannableString(getString(R.string.main_for, durationString))
                Crashlytics.log("main activity, log set span 149 was \"$debugText\" it is now \"$durationString\"")
                spannable.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        mainActivityPresenter.onUserWantsToSeeSelectTimer()
                    }

                }, 4, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                Crashlytics.log("main activity, log set span 157 was \"$debugText\" it is now \"$durationString\"")
                spannable.setSpan(
                    RelativeSizeSpan(1.2f),
                    4,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                forTimeText.apply {
                    text = spannable
                    movementMethod = LinkMovementMethod.getInstance()
                }
            }
        } catch (e:IndexOutOfBoundsException) {
            e.printStackTrace()
            Crashlytics.logException(e)
            Crashlytics.log("error caught on top new string  $durationString")
        }
    }

    override fun onTimeChanged(seconds: Int, minutes: Int) {
        val duration = Duration.ofMinutes(minutes.toLong()).plusSeconds(seconds.toLong())
        mainActivityPresenter.onUserWantsToConvert(duration)
    }

    override fun showUserMicrowaveWattage(userMicrowaveWattage: Int) {
        try {
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

        } catch (e:IndexOutOfBoundsException) {
            e.printStackTrace()
            Crashlytics.logException(e)
            Crashlytics.log("error caught on top new wattage  $userMicrowaveWattage")
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

    override fun showTimerSet() {
        Snackbar.make(numberPicker, getString(R.string.main_timerset_snackbar), Snackbar.LENGTH_SHORT).show()
    }

    override fun showSettingsScreen() {
        SettingsActivity.start(this)
    }

    override fun showPreviousProductWattage(wattage: Int) {
        defaultPosition = (wattage / 50) - 1
    }
}
