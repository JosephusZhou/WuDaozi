package com.josephuszhou.wudaozi.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
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

    private var mPhotoEntity: PhotoEntity? = null

    private var resize: Int

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

        mIvThumbnail.setOnClickListener(this)

        val count = Config.getInstance().mColumnsCount
        val divSize = context.resources.getDimensionPixelSize(R.dimen.wudaozi_grid_div_size)
        resize = (SizeUtil.getScreenWidth(context as Activity) - divSize * count) / count
    }

    fun setData(photoEntity: PhotoEntity) {
        mPhotoEntity = photoEntity
        Config.getInstance().mImageLoader.loadThumbnail(context, resize, thumbnailPlaceHolder, mIvThumbnail, mPhotoEntity?.uri)
    }

    override fun onClick(v: View?) {
        when(v) {
            mIvThumbnail -> {
                mPhotoEntity?.let {
                    Toast.makeText(context, it.id.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}