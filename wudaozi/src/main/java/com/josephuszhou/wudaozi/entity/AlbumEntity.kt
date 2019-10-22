package com.josephuszhou.wudaozi.entity

import android.os.Parcel
import android.os.Parcelable

class AlbumEntity(): Parcelable {

    var allPhoto = false
    var albumId = -1
    lateinit var albumName: String
    var photoCount = 0
    lateinit var thumbnail: PhotoEntity

    constructor(parcel: Parcel) : this() {
        allPhoto = parcel.readByte() != 0.toByte()
        albumId = parcel.readInt()
        albumName = parcel.readString() ?: ""
        photoCount = parcel.readInt()
        thumbnail = parcel.readParcelable(PhotoEntity::class.java.classLoader) ?: PhotoEntity()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (allPhoto) 1 else 0)
        parcel.writeInt(albumId)
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