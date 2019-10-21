package com.josephuszhou.wudaozi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.util.AttrUtil

/**
 * @author senfeng.zhou
 * @date 2019-10-14
 * @desc
 */
class CheckView: View {

    companion object {
        const val UNCHECKED_NUM = Int.MIN_VALUE
    }

    private var mStrokePaint = Paint()

    private var mBackgroundPaint = Paint()

    private var mTextPaint = Paint()

    private var mStrokeWidth: Float = context.resources.getDimension(R.dimen.wudaozi_check_stroke_width)

    private var mTextSize: Float = context.resources.getDimension(R.dimen.wudaozi_check_text_size)

    private var mStrokeColor: Int = AttrUtil.getcolor(context, R.attr.check_stroke_color, R.color.wudaozi_check_stroke_color)

    private var mFillColor: Int = AttrUtil.getcolor(context, R.attr.check_fill_color, R.color.wudaozi_check_fill_color)

    private var mTextColor: Int = AttrUtil.getcolor(context, R.attr.check_text_color, R.color.wudaozi_check_text_color)

    private var mEnabled: Boolean = true

    private var mCheckedNum: Int = UNCHECKED_NUM

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
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

        val drawWidth = width - paddingStart - paddingEnd

        if (mCheckedNum == UNCHECKED_NUM || mCheckedNum <= 0) {
            // draw normal status
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (drawWidth / 2).toFloat() - mStrokeWidth, mStrokePaint)
        } else {
            // draw selected status
            canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (drawWidth / 2).toFloat(), mBackgroundPaint)

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
        if (mCheckedNum != checkedNum) {
            mCheckedNum = checkedNum
            invalidate()
        }
    }

}