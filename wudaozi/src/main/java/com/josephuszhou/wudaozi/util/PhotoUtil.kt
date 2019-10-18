package com.josephuszhou.wudaozi.util

import android.app.Activity
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * @author senfeng.zhou
 * @date 2019-10-18
 * @desc
 */
class PhotoUtil {

    companion object {

        fun getPhotoSize(activity: Activity, uri: Uri) {
            val contentResolver = activity.contentResolver
            val photoSize = getBitmapBound(contentResolver, uri)
            var width = photoSize.x
            var height = photoSize.y
            if (photoRotated(contentResolver, uri)) {
                width = photoSize.y
                height = photoSize.x
            }

        }

        private fun getBitmapBound(contentResolver: ContentResolver, uri: Uri): Point {
            val point = Point(0, 0)

            var inputStream: InputStream? = null
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                inputStream = contentResolver.openInputStream(uri)
                inputStream?.let {
                    BitmapFactory.decodeStream(inputStream, null, options)
                    point.x = options.outWidth
                    point.y = options.outHeight
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                inputStream?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            return point
        }

        private fun photoRotated(contentResolver: ContentResolver, uri: Uri): Boolean {
            var rotated = false

            var inputStream: InputStream? = null
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                inputStream = contentResolver.openInputStream(uri)
                inputStream?.let {
                    val exifInterface = ExifInterface(it)
                    val orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    rotated = (orientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                            orientation == ExifInterface.ORIENTATION_ROTATE_270)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                inputStream?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            return false
        }
    }

}