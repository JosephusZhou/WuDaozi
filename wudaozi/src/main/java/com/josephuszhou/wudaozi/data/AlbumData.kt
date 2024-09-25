package com.josephuszhou.wudaozi.data

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
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
class AlbumData(activity: AppCompatActivity): LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val URL_LOADER = 10000
        private val IMAGE_URI: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private val PROJECTION: Array<String> = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
    }

    private var mContext: WeakReference<AppCompatActivity> = WeakReference(activity)

    private var mLoadManager: LoaderManager = LoaderManager.getInstance(activity)

    private val mAlbumList: ArrayList<AlbumEntity> = ArrayList()

    private var mCurrentAlbumEntity: AlbumEntity? = null

    private val mPhotoMap: HashMap<String, ArrayList<PhotoEntity>> = HashMap()

    private var mOnAlbumDataLoadListener: OnAlbumDataLoadListener? = null

    private var loadAlbums = 0

    fun load() {
        mLoadManager.initLoader(URL_LOADER, null, this)
    }

    fun destory() {
        mLoadManager.destroyLoader(URL_LOADER)
        for (albumEntity in mAlbumList) {
            try {
                mLoadManager.destroyLoader(albumEntity.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setOnLoadListener(onAlbumDataLoadListener: OnAlbumDataLoadListener) {
        mOnAlbumDataLoadListener = onAlbumDataLoadListener
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(getContext(), IMAGE_URI, PROJECTION, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data?.let {
            mAlbumList.clear()

            val allAlbumEntity = AlbumEntity(true, -1, getContext().getString(R.string.wudaozi_all), 0, PhotoEntity(-1, "", 0, "", Uri.EMPTY))
            mAlbumList.add(allAlbumEntity)

            val bucketColumn = it.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            var i = 0
            it.moveToFirst()
            do {
                val albumName = it.getString(bucketColumn)

                var exist = false
                for (albumEntity in mAlbumList) {
                    if (TextUtils.equals(albumEntity.albumName, albumName)) {
                        exist = true
                        break
                    }
                }
                if (!exist) {
                    mAlbumList.add(AlbumEntity(false, i + 1, albumName, 0, PhotoEntity(-1, "", 0, "", Uri.EMPTY)))
                }

                i ++
            } while (it.moveToNext())

            for (albumEntity in mAlbumList) {
                val photoData = if (albumEntity.id == -1) {
                    PhotoData(getContext(), "")
                } else {
                    PhotoData(getContext(), albumEntity.albumName)
                }
                photoData.apply {
                    setOnLoadListener(object : PhotoData.OnPhotoDataLoadListener {
                        override fun onPhotoDataLoaded() {
                            val list = photoData.getPhotoList()
                            albumEntity.photoCount = list.size
                            if (list.isNotEmpty()) {
                                albumEntity.thumbnail = list[0]
                            }
                            mPhotoMap[albumEntity.albumName] = list

                            photoData.destory(albumEntity.id)

                            loadAlbums ++
                            if (loadAlbums == mAlbumList.size) {
                                mOnAlbumDataLoadListener?.onAlbumDataLoaded()
                            }
                        }
                    })
                    load(albumEntity.id)
                }
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        // Not use the cursor directly, not need to handle the callback
    }

    private fun getContext() = mContext.get() as AppCompatActivity

    fun getAlbumList() = mAlbumList

    fun setCurrentAlbum(position: Int) {
        mCurrentAlbumEntity = mAlbumList[position]
    }

    fun getCurrentAlbumIndex(): Int {
        return mCurrentAlbumEntity?.let {
            for(i in mAlbumList.indices) {
                if (mAlbumList[i].albumName == it.albumName)
                    return@let i
            }
            0
        } ?: 0
    }

    fun getPhotoList(): ArrayList<PhotoEntity> {
        return mPhotoMap[mCurrentAlbumEntity?.albumName] ?: ArrayList()
    }

    interface OnAlbumDataLoadListener {
        fun onAlbumDataLoaded()
    }
}