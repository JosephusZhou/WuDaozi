package com.josephuszhou.wudaozi.view

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.adapter.AlbumAdapter
import com.josephuszhou.wudaozi.adapter.PhotoAdapter
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.AlbumEntity
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.SizeUtil
import kotlinx.android.synthetic.main.activity_wu_daozi.*

class WuDaoziActivity : AppCompatActivity(), AlbumSpinner.OnItemSelectedListener {

    private lateinit var albumList: ArrayList<AlbumEntity>
    private lateinit var photoList: ArrayList<PhotoEntity>
    private lateinit var albumSpinner: AlbumSpinner

    companion object {
        fun start(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(
                Intent(activity, WuDaoziActivity::class.java),
                requestCode
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wu_daozi)

        // init toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // init data
        albumList = ArrayList()
        photoList = ArrayList()

        // init spinner
        albumSpinner = AlbumSpinner(this)
        albumSpinner.apply {
            setAnchorView(tv_album)
            setSelectedTextView(tv_album)
            setOnItemSelectedListener(this@WuDaoziActivity)
        }

        // init recyclerview
        val count = Config.getInstance().mColumnsCount
        val divSize = SizeUtil.dp2px(this, 4f)
        val imageSize = (SizeUtil.getScreenWidth(this) - divSize * count) / count

        recyclerview.layoutManager = GridLayoutManager(this, count)
        recyclerview.addItemDecoration(
            GridItemDecoration(
                Config.getInstance().mColumnsCount,
                divSize,
                divSize,
                false
            )
        )
        recyclerview.adapter = PhotoAdapter(this, imageSize, photoList)

        queryData()
        showData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun queryData() {
        val externalURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cursor = contentResolver.query(
            externalURI,
            null,
            null,
            null,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        cursor?.let {
            var allAlbumEntity = AlbumEntity()
            allAlbumEntity.bucketName = getString(R.string.wudaozi_all)
            allAlbumEntity.photoCount = 0
            albumList.add(allAlbumEntity)

            var index = 0
            it.moveToLast()
            do {
                val bucketNameIndex = it.getColumnIndex("bucket_display_name")
                val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)

                val bucketName = it.getString(bucketNameIndex)
                val id = it.getLong(idIndex)

                val photoEntity = PhotoEntity()
                photoEntity.id = id
                photoEntity.uri = ContentUris.withAppendedId(externalURI, id)
                photoEntity.albumName = bucketName
                photoList.add(photoEntity)

                allAlbumEntity = albumList[0]
                allAlbumEntity.photoCount++
                if (index < 1) {
                    allAlbumEntity.thumbnail = photoEntity
                    index = 1
                }

                var exist = false
                for (albumEntity in albumList) {
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
                    albumList.add(albumEntity)
                }

            } while (it.moveToPrevious())
        }

        cursor?.close()
    }

    private fun showData() {
        albumSpinner.setAlbumAdapter(AlbumAdapter(this, albumList))
        albumSpinner.setSelected(0)
        onItemSelected(0)
    }

    override fun onItemSelected(position: Int) {
        if (position == 0) {
            (recyclerview.adapter as PhotoAdapter).setData(photoList)
            return
        }
        val albumEntity = albumList[position]
        val list = ArrayList<PhotoEntity>()
        for (entity in photoList) {
            if (TextUtils.equals(entity.albumName, albumEntity.bucketName)) {
                list.add(entity)
            }
        }
        (recyclerview.adapter as PhotoAdapter).setData(list)
    }
}
