package com.josephuszhou.wudaozi.entity

import android.net.Uri

class PhotoEntity {

    var id: Long = -1
    lateinit var uri: Uri
    lateinit var albumName: String

    var selected: Boolean = false
    var selectIndex: Int = 1
}