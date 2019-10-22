package com.josephuszhou.wudaozi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.viewpager.widget.ViewPager
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.adapter.PreviewPagerAdapter
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.data.PhotoData
import com.josephuszhou.wudaozi.data.SelectedData
import com.josephuszhou.wudaozi.entity.AlbumEntity
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.widget.CheckView
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity(), View.OnClickListener,
    ImageViewTouch.OnImageViewTouchSingleTapListener, ViewPager.OnPageChangeListener,
    PhotoData.OnLoadListener {

    companion object {
        const val REQUEST_CODE_PREVIEW: Int = 8888

        private const val ARGS_ALBUM_ENTITY = "argsAlbumEntity"

        private const val ARGS_PHOTO_ENTITY = "argsPhotoEntity"

        fun start(
            activity: Activity,
            requestCode: Int,
            albumEntity: AlbumEntity,
            photoEntity: PhotoEntity
        ) {
            val intent = Intent(activity, PreviewActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(ARGS_ALBUM_ENTITY, albumEntity)
                    putParcelable(ARGS_PHOTO_ENTITY, photoEntity)
                })
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var mSlectedAlbumEntity: AlbumEntity? = null

    private var mEntryPhotoEntity: PhotoEntity? = null

    private lateinit var mPhotoData: PhotoData

    private var mSelectedData = SelectedData.getInstance()

    private lateinit var mPreviewPagerAdapter: PreviewPagerAdapter

    private var mPrePosition = -1

    private var mBarViewHided = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Config.getInstance().mThemeId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mSlectedAlbumEntity = intent?.extras?.getParcelable(ARGS_ALBUM_ENTITY)
        mEntryPhotoEntity = intent?.extras?.getParcelable(ARGS_PHOTO_ENTITY)
        if (mSlectedAlbumEntity == null || mEntryPhotoEntity == null) {
            throw Exception("Can you just pass a entity to me?")
        }

        preview_view_pager.addOnPageChangeListener(this)
        mPreviewPagerAdapter = PreviewPagerAdapter(supportFragmentManager)
        preview_view_pager.adapter = mPreviewPagerAdapter

        check_view.setOnClickListener(this)
        tv_back.setOnClickListener(this)
        tv_sure.setOnClickListener(this)

        setSureTextStatus()

        mPhotoData = PhotoData(this).apply {
            setOnLoadListener(this@PreviewActivity)
            load()
        }
    }

    override fun onLoaded() {
        val photoList = mPhotoData.getPhotoList(mSlectedAlbumEntity!!)
        mPreviewPagerAdapter.setData(photoList)

        var entryIndex = 0
        for(i in photoList.indices) {
            if (photoList[i].id == mEntryPhotoEntity!!.id) {
                entryIndex = i
            }
        }
        preview_view_pager.setCurrentItem(entryIndex, false)
        mPrePosition = entryIndex
    }

    override fun onClick(v: View?) {
        when (v) {
            check_view -> {
                val photoEntity =
                mPreviewPagerAdapter.getAdapterItem(preview_view_pager.currentItem)
                val checkNum = mSelectedData.checkSelectedItem(photoEntity)
                if (checkNum > 0) {
                    mSelectedData.removeSelectedItem(photoEntity)
                } else {
                    if (!mSelectedData.maxSelected() &&
                        mSelectedData.isAcceptable(this@PreviewActivity, photoEntity)
                    ) {
                        mSelectedData.addSelectedItem(photoEntity)
                    }
                }
                setCheckStatus(photoEntity)
                setSureTextStatus()
            }
            tv_back -> {
                onBackPressed()
            }
            tv_sure -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setCheckStatus(entity: PhotoEntity) {
        val checkNum = mSelectedData.checkSelectedItem(entity)
        if (checkNum > 0) {
            check_view.isEnabled = true
            check_view.setCheckedNum(checkNum)
        } else {
            if (mSelectedData.maxSelected()) {
                check_view.isEnabled = false
                check_view.setCheckedNum(CheckView.UNCHECKED_NUM)
            } else {
                check_view.isEnabled = true
                check_view.setCheckedNum(CheckView.UNCHECKED_NUM)
            }
        }
    }

    private fun setSureTextStatus() {
        val selectedCount = mSelectedData.selectedCount()
        tv_sure.isEnabled = selectedCount > 0
        tv_sure.text = if (selectedCount > 0) {
            getString(R.string.wudaozi_sure_with_num, selectedCount)
        } else {
            getString(R.string.wudaozi_sure)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
    }

    override fun onPageSelected(position: Int) {
        if (mPrePosition != -1 && mPrePosition != position) {
            (mPreviewPagerAdapter.instantiateItem(
                preview_view_pager,
                mPrePosition
            ) as PreviewFragment).resetView()

            val currentPhotoEntity = mPreviewPagerAdapter.getAdapterItem(position)
            setCheckStatus(currentPhotoEntity)
        }
        mPrePosition = position
    }

    override fun onSingleTapConfirmed() {
        updateBarView()
    }

    private fun updateBarView() {
        if (mBarViewHided) {
            layout_top.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(layout_top.measuredHeight.toFloat())
                start()
            }
            layout_bottom.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(-layout_bottom.measuredHeight.toFloat())
                start()
            }
        } else {
            layout_top.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(-layout_top.measuredHeight.toFloat())
                start()
            }
            layout_bottom.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(layout_bottom.measuredHeight.toFloat())
                start()
            }
        }
        mBarViewHided = !mBarViewHided
    }
}
