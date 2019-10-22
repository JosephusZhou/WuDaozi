package com.josephuszhou.wudaozi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import it.sephiroth.android.library.imagezoom.ImageViewTouch

/**
 * @author senfeng.zhou
 * @date 2019-10-21
 * @desc
 */
class PreviewViewPager: ViewPager {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun canScroll(v: View?, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        if (v is ImageViewTouch) {
            return v.canScroll(dx) || super.canScroll(v, checkV, dx, x, y)
        }
        return super.canScroll(v, checkV, dx, x, y)
    }

}