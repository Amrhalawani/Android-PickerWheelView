package com.amrh.verticalpicker

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.amrh.verticalpicker.listeners.OnItemSelectedListener

class PickerLayoutManager(context: Context?) : LinearLayoutManager(context) {


    init {
        orientation = RecyclerView.VERTICAL
    }

    var callback: OnItemSelectedListener? = null
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!

        // Smart snapping
        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        scaleDownDependonEccentricity()
    }


    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            scaleDownDependonEccentricity()
            scrolled
        } else {
            0
        }

    }

    private fun scaleDownDependonEccentricity() {
        val SCALE_FACTOR:Float = 0.9F // 0.0 means no scaling
        for (i in 0 until childCount) {

            val child = getChildAt(i)

            val scale = getChildScale(child!!,SCALE_FACTOR)

            child.scaleX = scale
            child.scaleY = scale
            child.alpha = scale
        }
    }

    private fun getChildScale(child: View,scaleFactor:Float):Float {
        // The scaling formula
        return 1 - Math.sqrt(( getDistancefromCentertoChildCenter(child) / height).toDouble()).toFloat() * scaleFactor
    }

    private fun getDistancefromCentertoChildCenter(child: View): Float {
        val centerofHorizontalaxis = height / 2.0f
        val childCenter = (getDecoratedTop(child) + getDecoratedBottom(child)) / 2.0f
        return Math.abs(centerofHorizontalaxis - childCenter)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // When scroll stops we notify on the selected item
        if (state.equals(RecyclerView.SCROLL_STATE_IDLE)) {

            // closest child to the recyclerView center --> this is the selected item.

            val centerYofrecyclerView = getRecyclerViewCenterY() //the  calculated value: measured from up to down, (0,0) in top left
            var minDistance = recyclerView.height
            var position = -1
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterY = getChildCenterY(child)
                val childandRecyclerClearance = Math.abs(childCenterY - centerYofrecyclerView)
                if (childandRecyclerClearance < minDistance) {
                    minDistance = childandRecyclerClearance
                    position = recyclerView.getChildLayoutPosition(child)
                }
            }

            // Notify on item selection
            callback?.onItemSelected(position)
        }
    }

    private fun getChildCenterY(child: View): Int {
        return getDecoratedTop(child) + ((getDecoratedBottom(child) - getDecoratedTop(child)) / 2) + recyclerView.top
    }


    private fun getRecyclerViewCenterY(): Int {
        return (recyclerView.bottom - recyclerView.top) / 2 + recyclerView.top
    }
    interface OnItemSelectedListener {
        fun onItemSelected(layoutPosition: Int)
    }
}