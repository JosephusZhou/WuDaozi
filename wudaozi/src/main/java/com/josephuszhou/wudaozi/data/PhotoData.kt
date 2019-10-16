package com.josephuszhou.wudaozi.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.entity.AlbumEntity
import com.josephuszhou.wudaozi.entity.PhotoEntity
import java.lang.ref.WeakReference

/**
 * @author senfeng.zhou
 * @date 2019-10-16
 * @desc
 */
class PhotoData(@NonNull fragmentActivity: FragmentActivity): LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val URL_LOADER = 9
        private val IMAGE_URI: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private const val ORDER_BY: String = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
    }

    private var mContext: WeakReference<Context> = WeakReference(fragmentActivity)

    private var mLoadManager: LoaderManager = LoaderManager.getInstance(fragmentActivity)

    private lateinit var mAlbumList: ArrayList<AlbumEntity>

    private lateinit var mPhotoList: ArrayList<PhotoEntity>

    private var mOnLoadListener: OnLoadListener? = null

    fun load() {
        mLoadManager.initLoader(URL_LOADER, null, this)
    }

    fun destory() {
        mLoadManager.destroyLoader(URL_LOADER)
    }

    fun setOnLoadListener(onLoadListener: OnLoadListener) {
        mOnLoadListener = onLoadListener
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(getContext(), IMAGE_URI, null, null, null, ORDER_BY)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.let {
            mAlbumList = ArrayList()
            mPhotoList = ArrayList()

            var allAlbumEntity = AlbumEntity()
            allAlbumEntity.bucketName = getContext().getString(R.string.wudaozi_all)
            allAlbumEntity.photoCount = 0
            mAlbumList.add(allAlbumEntity)

            it.moveToFirst()
            do {
                val bucketNameIndex = it.getColumnIndex("bucket_display_name")
                val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
                val sizeIndex = it.getColumnIndex(MediaStore.Images.Media.SIZE)
                val mimeTypeIndex = it.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)

                val bucketName = it.getString(bucketNameIndex)
                val id = it.getLong(idIndex)
                val size = it.getInt(sizeIndex)
                val mimeType = it.getString(mimeTypeIndex)

                val photoEntity = PhotoEntity()
                photoEntity.id = id
                photoEntity.uri = ContentUris.withAppendedId(IMAGE_URI, id)
                photoEntity.albumName = bucketName
                photoEntity.size = size
                photoEntity.mimeType = mimeType
                mPhotoList.add(photoEntity)

                allAlbumEntity = mAlbumList[0]
                allAlbumEntity.photoCount++
                if (allAlbumEntity.photoCount == 1) {
                    allAlbumEntity.thumbnail = photoEntity
                }

                var exist = false
                for (albumEntity in mAlbumList) {
                    if (TextUtils.equals(albumEntity.bucketName, bucketName)) {
                        exist = true
                        albumEntity.photoCount++
                        break
                    }
                }
                if (!exist) {
                    val albumEntity = AlbumEntity()
                    albumEntity.bucketName = bucketName
                    albumEntity.photoCount = 1
                    albumEntity.thumbnail = photoEntity
                    mAlbumList.add(albumEntity)
                }

            } while (it.moveToNext())
        }

        mOnLoadListener?.onLoaded()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Not use the cursor directly, not need to handle the callback
    }

    private fun getContext() = (mContext.get() as Context)

    fun getAlbumList() = mAlbumList

    fun getPhotoList() = mPhotoList

    interface OnLoadListener {
        fun onLoaded()
    }

}