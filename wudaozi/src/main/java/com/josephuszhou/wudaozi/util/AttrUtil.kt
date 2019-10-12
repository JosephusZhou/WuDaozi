package com.josephuszhou.wudaozi.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes

/**
 * @author senfeng.zhou
 * @date 2019-10-12
 * @desc
 */
class AttrUtil {

    companion object {
        fun getcolor(context: Context, @AttrRes attrId: Int): Int {
            val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
            val color = typedArray.getColor(0, 0)
            typedArray.recycle()
            return color
        }

        fun getDrawable(context: Context, @AttrRes attrId: Int): Drawable? {
            val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
            val drawable = typedArray.getDrawable(0)
            typedArray.recycle()
            return  drawable
        }
    }

}