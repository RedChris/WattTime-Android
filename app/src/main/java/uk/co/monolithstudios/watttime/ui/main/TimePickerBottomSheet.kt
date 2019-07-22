package uk.co.monolithstudios.watttime.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_numberpicker_bottomsheet.*
import uk.co.monolithstudios.watttime.R
import java.text.DecimalFormat

private const val ARG_MINUTES = "minutes"
private const val ARG_SECONDS = "seconds"

class TimePickerBottomSheet : Fragment() {

    private var minutes: Int? = null
    private var seconds: Int? = null
    private var listener: OnFragmentInteractionListener? = null
    private val formatter = DecimalFormat("00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            minutes = it.getInt(ARG_MINUTES)
            seconds = it.getInt(ARG_SECONDS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_numberpicker_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        secondsPicker.apply {
            minValue = 0
            maxValue = 60
            setFormatter { formatter.format(it) }
            setOnValueChangedListener { _, _, newVal ->
                seconds = newVal
                listener?.onTimeChanged(seconds!!, minutes!!)
            }
            seconds?.let { value = seconds as Int }
        }

        minutesPicker.apply {
            minValue = 0
            maxValue = 60
            setFormatter { formatter.format(it) }
            setOnValueChangedListener { _, _, newVal ->
                minutes = newVal
                listener?.onTimeChanged(seconds!!, minutes!!)
            }
            seconds?.let { value = seconds as Int }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onTimeChanged(seconds: Int, minutes: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(minutes: Int, seconds: Int) =
            TimePickerBottomSheet().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MINUTES, minutes)
                    putInt(ARG_SECONDS, seconds)
                }
            }
    }
}
