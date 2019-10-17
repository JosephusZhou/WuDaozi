package com.josephuszhou.wudaozi.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

/**
 * @author senfeng.zhou
 * @date 2019-10-12
 * @desc
 */
class AttrUtil {

    companion object {
        fun getcolor(context: Context, @AttrRes attrId: Int, @ColorRes defaultColorId: Int): Int {
            val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
            val color = typedArray.getColor(0, ResourcesCompat.getColor(
                context.resources, defaultColorId, context.theme))
            typedArray.recycle()
            return color
        }

        fun getDrawable(context: Context, @AttrRes attrId: Int): Drawable? {
            val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
            val drawable = typedArray.getDrawable(0)
            typedArray.recycle()
            return  drawable
        }

        fun getString(context: Context, @AttrRes attrId: Int, @StringRes defaultStringId: Int): String {
            val typedArray = context.theme.obtainStyledAttributes(intArrayOf(attrId))
            val str = typedArray.getString(0)
            typedArray.recycle()
            return str ?: context.getString(defaultStringId)
        }
    }

}