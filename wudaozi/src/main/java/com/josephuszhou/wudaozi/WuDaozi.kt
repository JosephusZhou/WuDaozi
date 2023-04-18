package com.josephuszhou.wudaozi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.data.SelectedData
import com.josephuszhou.wudaozi.filter.Filter
import com.josephuszhou.wudaozi.imageloader.ImageLoader
import com.josephuszhou.wudaozi.view.WuDaoziActivity

class WuDaozi private constructor(private val context: Context) {

    companion object {

        const val BUNDLE_KEY: String = "wudaozi_uri_list"

        @JvmStatic
        fun with(context: Context) = WuDaozi(context)

        fun getLauncher(
            activity: ComponentActivity,
            callback: ActivityResultCallback<ArrayList<Uri>?>
        ): ActivityResultLauncher<Intent> {
            return activity.registerForActivityResult(SelectResult(), callback)
        }

        fun getLauncher(
            fragment: Fragment,
            callback: ActivityResultCallback<ArrayList<Uri>?>
        ): ActivityResultLauncher<Intent> {
            return fragment.registerForActivityResult(SelectResult(), callback)
        }
    }

    private val mConfig = Config.getInitialInstance()

    private val mSelectedData = SelectedData.getInitialInstance()

    fun theme(@StyleRes themeId: Int): WuDaozi {
        mConfig.mThemeId = themeId
        return this
    }

    fun imageLoader(imageLoader: ImageLoader): WuDaozi {
        mConfig.mImageLoader = imageLoader
        return this
    }

    fun columnsCount(@IntRange(from = 1) columnsCount: Int): WuDaozi {
        mConfig.mColumnsCount = columnsCount
        return this
    }

    fun maxSelectableCount(@IntRange(from = 1) maxSelectedCount: Int): WuDaozi {
        mConfig.mMaxSelectableCount = maxSelectedCount
        return this
    }

    fun filter(
        @IntRange(from = 0) minByteSize: Int = Filter.Size.NO_FILTER_SIZE,
        @IntRange(from = 0) maxByteSize: Int = Filter.Size.NO_FILTER_SIZE,
        selectedTypes: Array<String> = arrayOf(Filter.Type.ALL)
    ): WuDaozi {
        var size: Filter.Size? = null
        var type: Filter.Type? = null
        if (minByteSize != Filter.Size.NO_FILTER_SIZE || maxByteSize != Filter.Size.NO_FILTER_SIZE) {
            size = Filter.Size(minByteSize, maxByteSize)
        }
        if (!selectedTypes.contains(Filter.Type.ALL)) {
            type = Filter.Type(selectedTypes)
        }
        mConfig.mFilter = Filter(size, type)
        return this
    }

    fun start(launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(Intent(context, WuDaoziActivity::class.java))
    }

    class SelectResult : ActivityResultContract<Intent, ArrayList<Uri>?>() {

        override fun createIntent(context: Context, input: Intent): Intent = input

        override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<Uri>? {
            return if (resultCode == Activity.RESULT_OK) {
                intent?.extras?.getParcelableArrayList<Uri>(BUNDLE_KEY) as ArrayList<Uri>
            } else {
                null
            }
        }
    }

}