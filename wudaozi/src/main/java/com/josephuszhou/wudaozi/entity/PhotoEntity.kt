package com.josephuszhou.wudaozi.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class PhotoEntity() : Parcelable {

    var id: Long = -1
    lateinit var uri: Uri
    var size: Int = -1
    lateinit var mimeType: String

    var albumId = -1
    lateinit var albumName: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        uri = parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY
        size = parcel.readInt()
        mimeType = parcel.readString() ?: ""
        albumId = parcel.readInt()
        albumName = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(uri, flags)
        parcel.writeInt(size)
        parcel.writeString(mimeType)
        parcel.writeInt(albumId)
        parcel.writeString(albumName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoEntity> {
        override fun createFromParcel(parcel: Parcel): PhotoEntity {
            return PhotoEntity(parcel)
        }

        override fun newArray(size: Int): Array<PhotoEntity?> {
            return arrayOfNulls(size)
        }
    }
}