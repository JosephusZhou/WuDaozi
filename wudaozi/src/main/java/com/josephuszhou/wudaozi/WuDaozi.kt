package com.josephuszhou.wudaozi

import android.app.Activity
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.data.SelectedData
import com.josephuszhou.wudaozi.filter.Filter
import com.josephuszhou.wudaozi.imageloader.ImageLoader
import com.josephuszhou.wudaozi.view.WuDaoziActivity
import java.lang.ref.WeakReference

class WuDaozi private constructor(activity: Activity) {

    companion object {

        const val REQUEST_CODE: Int = 9999

        const val BUNDLE_KEY: String = "wudaozi_uri_list"

        fun with(activity: Activity) = WuDaozi(activity)

        fun with(fragment: Fragment): WuDaozi {
            fragment.activity?.let {
                return with(it)
            } ?: throw IllegalStateException("No Activity attached")
        }

    }

    private val mActivityReference = WeakReference<Activity>(activity)

    private val mConfig = Config.getInitialInstance()

    private val mSelectedData = SelectedData.getInitialInstance()

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

    fun maxSelectableCount(@IntRange(from = 1) maxSelectedCount: Int): WuDaozi {
        mConfig.mMaxSelectableCount = maxSelectedCount
        return this
    }

    fun filter(@IntRange(from = 0) minByteSize: Int = Filter.Size.NO_FILTER_SIZE,
               @IntRange(from = 0) maxByteSize: Int = Filter.Size.NO_FILTER_SIZE,
               selectedTypes: Array<String> = arrayOf(Filter.Type.ALL)): WuDaozi {
        var size: Filter.Size? = null
        var type: Filter.Type? = null
        if (minByteSize != Filter.Size.NO_FILTER_SIZE || maxByteSize != Filter.Size.NO_FILTER_SIZE) {
            size = Filter.Size(minByteSize, maxByteSize)
        }
        if (!selectedTypes.contains(Filter.Type.ALL)) {
            type = Filter.Type(selectedTypes)
        }
        mConfig.mFilter = Filter(size, type)
        return this
    }

    fun start() {
        mActivityReference.get()?.let {
            WuDaoziActivity.start(it, REQUEST_CODE)
        }
    }

}