package com.josephuszhou.wudaozi.view

import com.josephuszhou.wudaozi.entity.PhotoEntity

/**
 * @author senfeng.zhou
 * @date 2024/9/25
 * @desc
 */
internal class GlobalShare {

    companion object {

        @Volatile
        private var INSTANCE: GlobalShare? = null

        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalShare().also { INSTANCE = it }
            }
    }

    var photoList: ArrayList<PhotoEntity>? = null

}