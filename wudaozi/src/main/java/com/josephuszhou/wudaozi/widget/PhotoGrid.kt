package com.josephuszhou.wudaozi.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.AttrUtil
import com.josephuszhou.wudaozi.util.SizeUtil

/**
 * @author senfeng.zhou
 * @date 2019-10-14
 * @desc
 */
class PhotoGrid : SquareFrameLayout, View.OnClickListener {

    private val thumbnailPlaceHolder = AttrUtil.getDrawable(context, R.attr.thumbnail_placeholder)

    private var mIvThumbnail: AppCompatImageView

    private var mCheckView: CheckView

    private var mMaskView: View

    private var mPhotoEntity: PhotoEntity? = null

    private var mSize: Int

    private var mOnPhotoGridClickListener: OnPhotoGridClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_photo_grid, this, true)
        mIvThumbnail = findViewById(R.id.iv_photo_thumbnail)
        mMaskView = findViewById(R.id.v_mask)
        mCheckView = findViewById(R.id.check_view)

        mIvThumbnail.setOnClickListener(this)
        mCheckView.setOnClickListener(this)

        val count = Config.getInstance().mColumnsCount
        val divSize = context.resources.getDimensionPixelSize(R.dimen.wudaozi_grid_div_size)
        mSize = (SizeUtil.getScreenWidth(context as Activity) - divSize * count) / count

        val paddingSize = context.resources.getDimensionPixelSize(R.dimen.wudaozi_thumbnial_check_padding)
        mCheckView.layoutParams.width = mSize / 4 + paddingSize * 2
        mCheckView.layoutParams.height = mSize / 4 + paddingSize * 2
    }

    fun setData(photoEntity: PhotoEntity) {
        mPhotoEntity = photoEntity
        Config.getInstance().mImageLoader.loadThumbnail(context, mSize, thumbnailPlaceHolder, mIvThumbnail, mPhotoEntity?.uri)
    }

    fun setCheckEnabled(enabled: Boolean) {
        mCheckView.isEnabled = enabled
    }

    fun setCheckNum(num: Int) {
        mCheckView.setCheckedNum(num)
        if (num > 0) {
            mMaskView.visibility = View.VISIBLE
        } else {
            mMaskView.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            mIvThumbnail -> {
                mOnPhotoGridClickListener?.let {
                    mPhotoEntity?.let { entity ->
                        it.onThumbnailClick(mIvThumbnail, entity)
                    }
                }
            }
            mCheckView -> {
                mOnPhotoGridClickListener?.let {
                    mPhotoEntity?.let { entity ->
                        it.onCheckViewClick(mCheckView, entity)
                    }
                }
            }
        }
    }

    fun setOnPhotoGridClickListener(onPhotoGridClickListener: OnPhotoGridClickListener) {
        mOnPhotoGridClickListener = onPhotoGridClickListener
    }

    interface OnPhotoGridClickListener {
        fun onThumbnailClick(thumbnail: AppCompatImageView, photoEntity: PhotoEntity)
        fun onCheckViewClick(checkView: CheckView, photoEntity: PhotoEntity)
    }

}