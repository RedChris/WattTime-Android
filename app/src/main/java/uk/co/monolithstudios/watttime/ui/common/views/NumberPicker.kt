package uk.co.monolithstudios.watttime.ui.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_numberpicker.view.*
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.domain.ScreenUtils

class NumberPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val data = (1..20).toList().map { it.toString() } as ArrayList<String>
    private val sliderLayoutManager: SliderLayoutManager

    init {
        View.inflate(context, R.layout.view_numberpicker, this)

        val padding: Int = ScreenUtils.getScreenWidth(context)/2 - ScreenUtils.dpToPx(context, 40)
        vertical_picker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        sliderLayoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                }
            }
        }
        vertical_picker.layoutManager = sliderLayoutManager

                // Setting Adapter
        vertical_picker.adapter = SliderAdapter().apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    vertical_picker.smoothScrollToPosition(vertical_picker.getChildLayoutPosition(view))
                }
            }
        }
    }

    fun setOnItemSelectedListener(onItemSelectedListener: SliderLayoutManager.OnItemSelectedListener) {
        sliderLayoutManager.callback = onItemSelectedListener
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

}