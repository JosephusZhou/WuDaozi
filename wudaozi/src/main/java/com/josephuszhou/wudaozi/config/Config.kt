package com.josephuszhou.wudaozi.config

import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.imageloader.ImageLoader
import com.josephuszhou.wudaozi.imageloader.impl.GlideLoader

class Config {

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

    fun reset() {
        mThemeId = R.style.WuDaozi_Theme
        mImageLoader = GlideLoader()
        mColumnsCount = 4
    }

}