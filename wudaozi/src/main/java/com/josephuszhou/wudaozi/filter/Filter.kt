package com.josephuszhou.wudaozi.filter

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.IntRange
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.AttrUtil

/**
 * @author senfeng.zhou
 * @date 2019-10-16
 * @desc
 */
class Filter(private var mSize: Size? = null, private var mType: Type? = null) {

    fun filter(context: Context, entity: PhotoEntity): Boolean {
        return mSize?.let { s ->
            mType?.let { t ->
                s.filter(context, entity.size).handleResult(context) &&
                        t.filter(context, entity.mimeType).handleResult(context)
            } ?: s.filter(context, entity.size).handleResult(context)
        } ?: mType?.filter(context, entity.mimeType)?.handleResult(context) ?: true
    }

    /**
     * Size class, unit is B
     */
    class Size(
        @IntRange(from = 0) private var mMinSize: Int = NO_FILTER_SIZE,
        @IntRange(from = 0) private var mMaxSize: Int = NO_FILTER_SIZE
    ) {

        companion object {
            // if the size of a image is 0B, is wrong
            const val NO_FILTER_SIZE = 0
            // the smallest picture is set to 1B
            private const val MIN_FILTER_SIZE = 1
        }

        private fun converSize(size: Int): String {
            return if (size < 1024) {
                "${size}B"
            } else {
                val kbSize = size / 1024
                if (kbSize < 1024) {
                    "${kbSize}KB"
                } else {
                    val mbSize = kbSize / 1024
                    "${mbSize}MB"
                }
            }
        }

        fun filter(context: Context, size: Int): Result {
            return if (mMinSize == NO_FILTER_SIZE) {
                if (mMaxSize == NO_FILTER_SIZE) {
                    Result()
                } else {
                    if (size in MIN_FILTER_SIZE..mMaxSize) {
                        Result()
                    } else {
                        Result(
                            false, String.format(
                                AttrUtil.getString(
                                    context,
                                    R.attr.larger_than_maxsize_text,
                                    R.string.wudaozi_larger_than_maxsize
                                ), converSize(mMaxSize)
                            )
                        )
                    }
                }
            } else if (mMaxSize == NO_FILTER_SIZE) {
                if (size >= mMinSize) {
                    Result()
                } else {
                    Result(
                        false, String.format(
                            AttrUtil.getString(
                                context,
                                R.attr.smaller_than_minsize_text,
                                R.string.wudaozi_smaller_than_minsize
                            ), converSize(mMinSize)
                        )
                    )
                }
            } else {
                if (size in mMinSize..mMaxSize) {
                    Result()
                } else {
                    Result(
                        false, String.format(
                            AttrUtil.getString(
                                context,
                                R.attr.between_minsize_maxsize_text,
                                R.string.wudaozi_between_minsize_maxsize
                            ), converSize(mMinSize),
                            converSize(mMaxSize)
                        )
                    )
                }
            }
        }
    }

    /**
     * Type class
     */
    class Type(private var mTypes: Array<String>) {

        companion object {
            const val ALL = "image/*"

            const val JPG = "image/jpeg"
            const val PNG = "image/png"
            const val GIF = "image/gif"
            const val BMP = "image/x-ms-bmp"
            const val WEBP = "image/webp"
        }

        fun filter(context: Context, mimeType: String): Result {
            if (mTypes.isEmpty())
                return Result()

            for (type in mTypes) {
                if (TextUtils.equals(mimeType, type)) {
                    return Result()
                }
            }

            return Result(false, AttrUtil.getString(
                context,
                R.attr.unsupported_image_type_text,
                R.string.wudaozi_unsupported_image_type
            ))
        }
    }

    /**
     * Result class
     */
    class Result(private var flag: Boolean = true, private var message: String = "") {

        fun handleResult(context: Context): Boolean {
            if (!flag) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            return flag
        }

    }

}