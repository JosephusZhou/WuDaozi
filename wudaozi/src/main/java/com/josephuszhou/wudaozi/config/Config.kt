package com.josephuszhou.wudaozi.config

import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.filter.Filter
import com.josephuszhou.wudaozi.imageloader.ImageLoader
import com.josephuszhou.wudaozi.imageloader.impl.GlideLoader

class Config private constructor() {

    companion object {
        fun getInstance() = InstanceHolder.INSTANCE

        fun getInitialInstance(): Config {
            val instance = getInstance()
            instance.reset()
            return instance
        }
    }

    private object InstanceHolder {
        val INSTANCE = Config()
    }

    var mThemeId: Int = R.style.WuDaozi_Theme

    var mImageLoader: ImageLoader = GlideLoader()

    var mColumnsCount: Int = 4

    var mMaxSelectableCount: Int = 1

    var mFilter: Filter? = null

    fun reset() {
        mThemeId = R.style.WuDaozi_Theme
        mImageLoader = GlideLoader()
        mColumnsCount = 4
        mMaxSelectableCount = 1
        mFilter = null
    }

}