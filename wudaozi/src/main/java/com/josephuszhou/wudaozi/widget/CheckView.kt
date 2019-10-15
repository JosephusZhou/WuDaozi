package com.josephuszhou.wudaozi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.josephuszhou.wudaozi.R

/**
 * @author senfeng.zhou
 * @date 2019-10-14
 * @desc
 */
class CheckView: View {

    companion object {
        private const val UNCHECKED_NUM = Int.MIN_VALUE
    }

    private var mStrokePaint = Paint()

    private var mBackgroundPaint = Paint()

    private var mTextPaint = Paint()

    private var mStrokeWidth: Float = context.resources.getDimension(R.dimen.wudaozi_check_stroke_width)

    private var mTextSize: Float = context.resources.getDimension(R.dimen.wudaozi_check_text_size)

    private var mStrokeColor: Int

    private var mFillColor: Int

    private var mTextColor: Int

    private var mEnabled: Boolean = true

    private var mCheckedNum: Int = UNCHECKED_NUM

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        var typedArray = context.theme.obtainStyledAttributes(intArrayOf(R.attr.check_stroke_color))
        mStrokeColor = typedArray.getColor(0, ResourcesCompat.getColor(resources, R.color.wudaozi_check_stroke_color, context.theme))
        typedArray.recycle()

        typedArray = context.theme.obtainStyledAttributes(intArrayOf(R.attr.check_fill_color))
        mFillColor = typedArray.getColor(0, ResourcesCompat.getColor(resources, R.color.wudaozi_check_fill_color, context.theme))
        typedArray.recycle()

        typedArray = context.theme.obtainStyledAttributes(intArrayOf(R.attr.check_text_color))
        mTextColor = typedArray.getColor(0, ResourcesCompat.getColor(resources, R.color.wudaozi_check_text_color, context.theme))
        typedArray.recycle()

        initStrokePaint()
        initBackgroundPaint()
        initTextPaint()
    }

    private fun initStrokePaint() {
        mStrokePaint.isAntiAlias = true
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = mStrokeWidth
        mStrokePaint.color = mStrokeColor
    }

    private fun initBackgroundPaint() {
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.style = Paint.Style.FILL
        mBackgroundPaint.color = mFillColor
    }

    private fun initTextPaint() {
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.color = mTextColor
        mTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        mTextPaint.textSize = mTextSize
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mCheckedNum == UNCHECKED_NUM || mCheckedNum <= 0) {
            // draw normal status
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat() - mStrokeWidth, mStrokePaint)
        } else {
            // draw selected status
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), mBackgroundPaint)

            // draw text
            val text = mCheckedNum.toString()
            val baseX = (width - mTextPaint.measureText(text)) / 2
            val baseY = (height -  mTextPaint.descent() - mTextPaint.ascent()) / 2
            canvas?.drawText(text, baseX, baseY, mTextPaint)
        }

        // draw enabled status
        alpha = if (mEnabled) 1.0f else 0.5f
    }

    override fun setEnabled(enabled: Boolean) {
        if (mEnabled != enabled) {
            mEnabled = enabled
            invalidate()
        }
    }

    fun setCheckedNum(checkedNum: Int) {
        if (checkedNum <= 0)
            return
        mCheckedNum = checkedNum
        invalidate()
    }

}