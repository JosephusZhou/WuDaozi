package com.josephuszhou.wudaozi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.data.SelectedData
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.SizeUtil
import com.josephuszhou.wudaozi.widget.CheckView
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_CODE_PREVIEW: Int = 8888

        fun start(activity: Activity, requestCode: Int, entity: PhotoEntity) {
            val intent = Intent(activity, PreviewActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable("entity", entity)
                })
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var mPhotoEntity: PhotoEntity? = null

    private var mSelectedData = SelectedData.getInstance()

    private var mBarViewHided = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Config.getInstance().mThemeId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mPhotoEntity = intent?.extras?.getParcelable("entity")
        if (mPhotoEntity == null) {
            throw Exception("Can you just pass a entity to me?")
        }

        iv_touch.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
        iv_touch.setSingleTapListener {
            updateBarView()
        }

        Config.getInstance().mImageLoader.loadImage(
            this,
            SizeUtil.getScreenWidth(this),
            SizeUtil.getScreenHeight(this),
            iv_touch,
            mPhotoEntity?.uri)

        check_view.setOnClickListener(this)
        tv_back.setOnClickListener(this)
        tv_sure.setOnClickListener(this)

        setCheckStatus()
    }

    override fun onClick(v: View?) {
        when (v) {
            check_view -> {
                mPhotoEntity?.let {
                    val checkNum = mSelectedData.checkSelectedItem(it)
                    if (checkNum > 0) {
                        mSelectedData.removeSelectedItem(it)
                    } else {
                        if (!mSelectedData.maxSelected() &&
                            mSelectedData.isAcceptable(this@PreviewActivity, it)) {
                            mSelectedData.addSelectedItem(it)
                        }
                    }
                    setCheckStatus()
                }
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

    private fun setCheckStatus() {
        mPhotoEntity?.let {
            val checkNum = mSelectedData.checkSelectedItem(it)
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
        setSureTextStatus()
    }

    private fun setSureTextStatus() {
        val selectedCount = mSelectedData.selectedCount()
        tv_sure.isEnabled = selectedCount > 0
        tv_sure.text = if (selectedCount > 0) {
            getString(R.string.wudaozi_sure_text_with_num, selectedCount)
        } else {
            getString(R.string.wudaozi_sure_text)
        }
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
