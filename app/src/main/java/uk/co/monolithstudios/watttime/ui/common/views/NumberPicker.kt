package uk.co.monolithstudios.watttime.ui.common.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
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
        vertical_picker.smoothScrollToPosition(0)
    }

    fun setOnItemSelectedListener(onItemSelectedListener: SliderLayoutManager.OnItemSelectedListener) {
        sliderLayoutManager.callback = onItemSelectedListener
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.position = sliderLayoutManager.getPosition()
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            goToPosition(state.position)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {

        var position: Int = 0

        constructor(source: Parcel) : super(source) {
            position = source.readByte().toInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeByte((position).toByte())
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}