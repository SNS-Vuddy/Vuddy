package com.b305.vuddy.util

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideItemAnimator : DefaultItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        if (oldHolder != newHolder) {
            return super.animateChange(oldHolder, newHolder, preLayoutInfo, postLayoutInfo)
        }

        val view = newHolder.itemView
        val animator = ObjectAnimator.ofFloat(view, "translationX", view.width.toFloat(), 0f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 500
        animator.start()

        return false
    }

    override fun getAddDuration(): Long {
        return 500
    }

    override fun getRemoveDuration(): Long {
        return 500
    }
}
