package com.josephuszhou.wudaozi.entity

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class AlbumEntity(
    var allPhoto: Boolean = false,
    var id: Int,
    var albumName: String,
    var photoCount: Int = 0,
    var thumbnail: PhotoEntity
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        thumbnail = parcel.readParcelable(PhotoEntity::class.java.classLoader) ?: PhotoEntity(-1, "", 0, "", Uri.EMPTY)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (allPhoto) 1 else 0)
        parcel.writeInt(id)
        parcel.writeString(albumName)
        parcel.writeInt(photoCount)
        parcel.writeParcelable(thumbnail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlbumEntity> {
        override fun createFromParcel(parcel: Parcel): AlbumEntity {
            return AlbumEntity(parcel)
        }

        override fun newArray(size: Int): Array<AlbumEntity?> {
            return arrayOfNulls(size)
        }
    }
}