package uk.co.monolithstudios.watttime.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.Duration
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.domain.TimeFormatter
import uk.co.monolithstudios.watttime.domain.TimerIntentLauncher
import uk.co.monolithstudios.watttime.domain.WattageCalculator
import uk.co.monolithstudios.watttime.ui.common.views.SliderLayoutManager
import uk.co.monolithstudios.watttime.ui.microwavesettings.MicrowaveSettingsActivity

class MainActivity : AppCompatActivity(), MainView, TimePickerBottomSheet.OnFragmentInteractionListener {

    lateinit var mainActivityPresenter: MainActivityPresenter
    private var wattArray: Array<String> = arrayOf()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wattArray = resources.getStringArray(R.array.wattages_array)
        mainActivityPresenter = MainActivityPresenter(this, Prefs(this), WattageCalculator(), TimerIntentLauncher(this), TimeFormatter(this))

        numberPicker.setOnItemSelectedListener(object : SliderLayoutManager.OnItemSelectedListener {
            override fun onItemSelected(layoutPosition: Int) {
                mainActivityPresenter.onUserWantsToSetProductWattage(wattArray[layoutPosition].toInt())
            }
        })

        numberPicker.setData(wattArray.toList())

        timerButton.setOnClickListener { mainActivityPresenter.onUserWantsToLaunchTimer() }

        val timeNumberPickerFragment = TimePickerBottomSheet.newInstance(0, 0)
        supportFragmentManager.beginTransaction().add(R.id.fragmentHolder, timeNumberPickerFragment).commit()
    }

    override fun showPackageWattage(wattage: Int) {
        foodPackageText.text = getString(R.string.main_foodPackageText).format(wattage)
    }

    override fun showPackageDuration(durationString: String) {
        forTimeText.text = getString(R.string.main_for, durationString)
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
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.material_blue_grey_950)), 20, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(1.5f),  20, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

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
