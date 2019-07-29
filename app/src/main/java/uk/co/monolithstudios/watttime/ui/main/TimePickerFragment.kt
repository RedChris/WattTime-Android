package uk.co.monolithstudios.watttime.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_timepicker.*
import uk.co.monolithstudios.watttime.R
import java.text.DecimalFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.app.Dialog
import android.opengl.Visibility
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val ARG_MINUTES = "minutes"
private const val ARG_SECONDS = "seconds"
private const val ARG_DIALOG_MODE = "dialog_mode"

class TimePickerFragment : BottomSheetDialogFragment() {

    private var dialogMode = false
    private var minutes: Int? = null
    private var seconds: Int? = null
    private var listener: OnFragmentInteractionListener? = null
    private val formatter = DecimalFormat("00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            minutes = it.getInt(ARG_MINUTES)
            seconds = it.getInt(ARG_SECONDS)
            dialogMode = it.getBoolean(ARG_DIALOG_MODE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet: FrameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }

        dialogMode = true
        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timepicker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton.visibility = if (dialogMode) View.VISIBLE else View.INVISIBLE

        setButton.setOnClickListener { if (dialogMode) dismiss() }

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

        setTime(minutes!!.toLong(), seconds!!.toLong())
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

    fun setTime(minutes: Long, seconds: Long) {
        minutesPicker.value = minutes.toInt()
        secondsPicker.value = seconds.toInt()
    }

    interface OnFragmentInteractionListener {
        fun onTimeChanged(seconds: Int, minutes: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(minutes: Int, seconds: Int, dialogMode: Boolean) =
            TimePickerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MINUTES, minutes)
                    putInt(ARG_SECONDS, seconds)
                    putBoolean(ARG_DIALOG_MODE, dialogMode)
                }
            }
    }
}
