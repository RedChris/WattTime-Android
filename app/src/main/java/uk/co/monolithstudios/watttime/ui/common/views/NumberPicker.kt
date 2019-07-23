package uk.co.monolithstudios.watttime.ui.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_numberpicker.view.*
import uk.co.monolithstudios.watttime.R
import uk.co.monolithstudios.watttime.domain.ScreenUtils



class NumberPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val data =  ArrayList<String>()
    private val sliderLayoutManager: SliderLayoutManager
    private val sliderAdapter: SliderAdapter

    init {
        View.inflate(context, R.layout.view_numberpicker, this)

        val padding: Int = ScreenUtils.getScreenWidth(context)/2 - ScreenUtils.dpToPx(context, 40)
        vertical_picker.setPadding(padding, 0, padding, 0)

        sliderLayoutManager = SliderLayoutManager(context).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                }
            }
        }
        vertical_picker.layoutManager = sliderLayoutManager

        sliderAdapter = SliderAdapter()
        vertical_picker.adapter = sliderAdapter.apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    vertical_picker.smoothScrollToPosition(vertical_picker.getChildLayoutPosition(view))
                }
            }
        }

        vertical_picker.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                sliderLayoutManager.notifyNewPosition()
            }
        })
    }

    fun goToPosition(position: Int) = vertical_picker.smoothScrollToPosition(position)

    fun setData(data: List<String>) {
        sliderAdapter.setData(data)
    }

    fun setOnItemSelectedListener(onItemSelectedListener: SliderLayoutManager.OnItemSelectedListener) {
        sliderLayoutManager.callback = onItemSelectedListener
    }

}