package com.josephuszhou.wudaozi.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

class SizeUtil {

    companion object {
        fun dp2px(context: Context, dpValue: Float): Int {
            val density = context.resources.displayMetrics.density
            return (dpValue * density + 0.5f).toInt()
        }

        fun getScreenWidth(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun getScreenHeight(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }

}