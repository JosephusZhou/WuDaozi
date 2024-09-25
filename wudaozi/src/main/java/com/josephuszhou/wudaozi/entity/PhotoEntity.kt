package com.josephuszhou.wudaozi.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class PhotoEntity(
    var id: Long,
    var mimeType: String,
    var size: Int,
    var data: String,
    var uri: Uri
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        uri = parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(mimeType)
        parcel.writeInt(size)
        parcel.writeString(data)
        parcel.writeParcelable(uri, flags)
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