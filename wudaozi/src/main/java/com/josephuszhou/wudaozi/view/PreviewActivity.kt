package com.josephuszhou.wudaozi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
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
import com.josephuszhou.wudaozi.widget.PreviewViewPager
import it.sephiroth.android.library.imagezoom.ImageViewTouch

class PreviewActivity : AppCompatActivity(), View.OnClickListener,
    ImageViewTouch.OnImageViewTouchSingleTapListener, ViewPager.OnPageChangeListener,
    PhotoData.OnLoadListener {

    companion object {
        private const val ARGS_ALBUM_ENTITY = "argsAlbumEntity"

        private const val ARGS_PHOTO_ENTITY = "argsPhotoEntity"

        fun start(
            activity: Activity,
            launcher: ActivityResultLauncher<Intent>,
            albumEntity: AlbumEntity,
            photoEntity: PhotoEntity
        ) {
            val intent = Intent(activity, PreviewActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(ARGS_ALBUM_ENTITY, albumEntity)
                    putParcelable(ARGS_PHOTO_ENTITY, photoEntity)
                })
            }
            launcher.launch(intent)
        }
    }

    private lateinit var previewViewPager: PreviewViewPager
    private lateinit var checkView: CheckView
    private lateinit var tvBack: AppCompatTextView
    private lateinit var tvSure: AppCompatTextView
    private lateinit var layoutTop: FrameLayout
    private lateinit var layoutBottom: FrameLayout

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

        previewViewPager = findViewById(R.id.preview_view_pager)
        checkView = findViewById(R.id.check_view)
        tvBack = findViewById(R.id.tv_back)
        tvSure = findViewById(R.id.tv_sure)
        layoutTop = findViewById(R.id.layout_top)
        layoutBottom = findViewById(R.id.layout_bottom)

        previewViewPager.addOnPageChangeListener(this)
        mPreviewPagerAdapter = PreviewPagerAdapter(supportFragmentManager)
        previewViewPager.adapter = mPreviewPagerAdapter

        checkView.setOnClickListener(this)
        tvBack.setOnClickListener(this)
        tvSure.setOnClickListener(this)

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
        previewViewPager.setCurrentItem(entryIndex, false)
        mPrePosition = entryIndex
    }

    override fun onClick(v: View?) {
        when (v) {
            checkView -> {
                val photoEntity =
                mPreviewPagerAdapter.getAdapterItem(previewViewPager.currentItem)
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
            tvBack -> {
                onBackPressed()
            }
            tvSure -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setCheckStatus(entity: PhotoEntity) {
        val checkNum = mSelectedData.checkSelectedItem(entity)
        if (checkNum > 0) {
            checkView.isEnabled = true
            checkView.setCheckedNum(checkNum)
        } else {
            if (mSelectedData.maxSelected()) {
                checkView.isEnabled = false
                checkView.setCheckedNum(CheckView.UNCHECKED_NUM)
            } else {
                checkView.isEnabled = true
                checkView.setCheckedNum(CheckView.UNCHECKED_NUM)
            }
        }
    }

    private fun setSureTextStatus() {
        val selectedCount = mSelectedData.selectedCount()
        tvSure.isEnabled = selectedCount > 0
        tvSure.text = if (selectedCount > 0) {
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
        if (mPrePosition != position) {
            if (mPrePosition != -1) {
                (mPreviewPagerAdapter.instantiateItem(
                    previewViewPager,
                    mPrePosition
                ) as PreviewFragment).resetView()
            }
            val currentPhotoEntity = mPreviewPagerAdapter.getAdapterItem(position)
            setCheckStatus(currentPhotoEntity)
            mPrePosition = position
        }
    }

    override fun onSingleTapConfirmed() {
        updateBarView()
    }

    private fun updateBarView() {
        if (mBarViewHided) {
            layoutTop.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(layoutTop.measuredHeight.toFloat())
                start()
            }
            layoutBottom.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(-layoutBottom.measuredHeight.toFloat())
                start()
            }
        } else {
            layoutTop.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(-layoutTop.measuredHeight.toFloat())
                start()
            }
            layoutBottom.animate().apply {
                interpolator = FastOutSlowInInterpolator()
                translationYBy(layoutBottom.measuredHeight.toFloat())
                start()
            }
        }
        mBarViewHided = !mBarViewHided
    }
}
