package uk.co.monolithstudios.watttime.ui.microwavesettings

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_microwave_settings.*
import kotlinx.android.synthetic.main.view_numberpicker.view.*
import uk.co.monolithstudios.watttime.Constants
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.ui.common.views.SliderLayoutManager
import uk.co.monolithstudios.watttime.ui.main.MainActivity

class MicrowaveSettingsActivity : AppCompatActivity(), MicrowaveSettingsView {

    private val KEY_LAYOUT_MANAGER_STATE = "keylayoutmanagerstate"

    private var mLayoutManagerState: Parcelable? = null

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MicrowaveSettingsActivity::class.java))
        }
    }

    lateinit var microwaveSettingsPresenter: MicrowaveSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_microwave_settings)

        val animationBackground = mainLayout.background as AnimationDrawable
        animationBackground.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(2000)
            start()
        }
        numberPicker.setData(Constants.wattages.map { it.toString() })
        if (savedInstanceState != null) {
            val layoutManagerState: Parcelable = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER_STATE)
            numberPicker.restoreInstance(layoutManagerState)
        } else {
            numberPicker.goToPosition(0)
        }

        microwaveSettingsPresenter = MicrowaveSettingsPresenter(this, Prefs(this))

        numberPicker.setOnItemSelectedListener(object : SliderLayoutManager.OnItemSelectedListener {
            override fun onItemSelected(layoutPosition: Int) {
                selectedWattsText.text =
                    getString(R.string.mainSettings_wattageLabel).format(Constants.wattages[layoutPosition])
                microwaveSettingsPresenter.onUserSelectedWattage(Constants.wattages[layoutPosition])
            }
        })

        saveButton.setOnClickListener { microwaveSettingsPresenter.onUserWantsToSaveWattage() }

    }

    override fun onPause() {
        super.onPause()
        mLayoutManagerState = numberPicker.saveInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, mLayoutManagerState)
    }

    override fun shoWattage(wattage: Int) {
        numberPicker.goToPosition((wattage / 50) - 1)
    }

    override fun setShowSaveButton(showSaveButton: Boolean) {
        saveButton.visibility = if (showSaveButton) View.VISIBLE else View.INVISIBLE
    }

    override fun showMainScreen() {
        MainActivity.start(this)
        finish()
    }
}
