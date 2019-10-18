package com.josephuszhou.wudaozi.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class PhotoEntity() : Parcelable {

    var id: Long = -1
    lateinit var uri: Uri
    lateinit var albumName: String
    var size: Int = -1
    lateinit var mimeType: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        uri = parcel.readParcelable<Uri>(Uri::class.java.classLoader) as Uri
        albumName = parcel.readString() as String
        size = parcel.readInt()
        mimeType = parcel.readString() as String
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(uri, flags)
        parcel.writeString(albumName)
        parcel.writeInt(size)
        parcel.writeString(mimeType)
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