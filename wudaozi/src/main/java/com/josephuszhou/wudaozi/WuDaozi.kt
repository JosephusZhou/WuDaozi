package com.josephuszhou.wudaozi

import android.app.Activity
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.imageloader.ImageLoader
import com.josephuszhou.wudaozi.view.WuDaoziActivity
import java.lang.ref.WeakReference

class WuDaozi private constructor(activity: Activity) {

    companion object {

        const val REQUEST_CODE: Int = 9999

        fun with(activity: Activity) = WuDaozi(activity)

        fun with(fragment: Fragment) {
            fragment.activity?.let {
                with(it)
            }
        }

    }

    private val mActivityReference = WeakReference<Activity>(activity)

    private val mConfig = Config.getInitialInstance()

    fun theme(@StyleRes themeId: Int):WuDaozi {
        mConfig.mThemeId = themeId
        return this
    }

    fun imageLoader(imageLoader: ImageLoader): WuDaozi {
        mConfig.mImageLoader = imageLoader
        return this
    }

    fun columnsCount(@IntRange(from = 1) columnsCount: Int): WuDaozi {
        mConfig.mColumnsCount = columnsCount
        return this
    }

    fun start() {
        mActivityReference.get()?.let {
            WuDaoziActivity.start(it, REQUEST_CODE)
        }
    }

}