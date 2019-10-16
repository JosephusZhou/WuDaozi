package com.josephuszhou.wudaozi.entity

import android.net.Uri

class PhotoEntity {

    var id: Long = -1
    lateinit var uri: Uri
    lateinit var albumName: String
    var size: Int = -1
    lateinit var mimeType: String
}