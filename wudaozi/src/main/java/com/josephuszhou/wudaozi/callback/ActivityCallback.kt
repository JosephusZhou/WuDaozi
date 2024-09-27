package com.josephuszhou.wudaozi.callback

import androidx.appcompat.app.AppCompatActivity

/**
 * @author senfeng.zhou
 * @date 2024/9/27
 * @desc
 */
interface ActivityCallback {

    fun onAlbumActivityOnCreate(activity: AppCompatActivity)

    fun onPreviewActivityOnCreate(activity: AppCompatActivity)

}