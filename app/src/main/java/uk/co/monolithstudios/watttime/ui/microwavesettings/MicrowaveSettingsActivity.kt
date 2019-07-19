package uk.co.monolithstudios.watttime.ui.microwavesettings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_microwave_settings.*
import kotlinx.android.synthetic.main.view_numberpicker.view.*
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.data.Prefs
import uk.co.monolithstudios.watttime.ui.common.views.SliderLayoutManager
import uk.co.monolithstudios.watttime.ui.main.MainActivity

class MicrowaveSettingsActivity : AppCompatActivity(), MicrowaveSettingsView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MicrowaveSettingsActivity::class.java))
        }
    }

    lateinit var microwaveSettingsPresenter: MicrowaveSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_microwave_settings)

        microwaveSettingsPresenter = MicrowaveSettingsPresenter(this, Prefs(this))

        numberPicker.setOnItemSelectedListener(object : SliderLayoutManager.OnItemSelectedListener {
            override fun onItemSelected(layoutPosition: Int) {
                selectedWattsText.text = getString(R.string.mainSettings_wattageLabel).format(layoutPosition)
                microwaveSettingsPresenter.onUserSelectedWattage(layoutPosition)
            }
        })

        saveButton.setOnClickListener { microwaveSettingsPresenter.onUserWantsToSaveWattage() }
    }

    override fun setShowSaveButton(showSaveButton: Boolean) {
        saveButton.visibility = if (showSaveButton) View.VISIBLE else View.INVISIBLE
    }

    override fun showMainScreen() {
        MainActivity.start(this)
        finish()
    }
}
