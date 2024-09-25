package com.josephuszhou.wudaozi.data

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.josephuszhou.wudaozi.entity.PhotoEntity
import java.lang.ref.WeakReference

/**
 * @author senfeng.zhou
 * @date 2019-10-16
 * @desc
 */
class PhotoData(activity: AppCompatActivity, albumName: String?): LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private val IMAGE_URI: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private val PROJECTION: Array<String> = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED
        )
        private const val ORDER_BY: String = MediaStore.Images.Media.DATE_ADDED + " DESC"
    }

    private var mContext: WeakReference<AppCompatActivity> = WeakReference(activity)

    private var mLoadManager: LoaderManager = LoaderManager.getInstance(activity)

    private lateinit var mPhotoList: ArrayList<PhotoEntity>

    private var mOnPhotoDataLoadListener: OnPhotoDataLoadListener? = null

    private val selection: String?
    private val selectionArgs: Array<String>?

    init {
        if (albumName.isNullOrEmpty()) {
            selection = null
            selectionArgs = null
        } else {
            selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?"
            selectionArgs = arrayOf(albumName)
        }
    }

    fun load(id: Int) {
        mLoadManager.initLoader(id, null, this)
    }

    fun destory(id: Int) {
        mLoadManager.destroyLoader(id)
    }

    fun setOnLoadListener(onPhotoDataLoadListener: OnPhotoDataLoadListener) {
        mOnPhotoDataLoadListener = onPhotoDataLoadListener
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(getContext(), IMAGE_URI, PROJECTION, selection, selectionArgs, ORDER_BY)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursor?.let {
            mPhotoList = ArrayList()

            val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
            val mimeTypeIndex = it.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
            val sizeIndex = it.getColumnIndex(MediaStore.Images.Media.SIZE)
            val dataIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            do {
                val id = it.getLong(idIndex)
                val mimeType = it.getString(mimeTypeIndex)
                val size = it.getInt(sizeIndex)
                val data = it.getString(dataIndex)

                val photoEntity = PhotoEntity(id, mimeType, size, data, ContentUris.withAppendedId(IMAGE_URI, id))
                mPhotoList.add(photoEntity)
            } while (it.moveToNext())
        }

        mOnPhotoDataLoadListener?.onPhotoDataLoaded()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Not use the cursor directly, not need to handle the callback
    }

    private fun getContext() = mContext.get() as AppCompatActivity

    fun getPhotoList(): ArrayList<PhotoEntity> {
        return mPhotoList
    }

    interface OnPhotoDataLoadListener {
        fun onPhotoDataLoaded()
    }
}