package com.josephuszhou.wudaozi.widget

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josephuszhou.wudaozi.adapter.PhotoAdapter
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.SizeUtil

/**
 * @author senfeng.zhou
 * @date 2019-10-12
 * @desc
 */
class PhotoGridView(context: Context, mRecyclerView: RecyclerView) {

    private var mPhotoAdapter: PhotoAdapter

    init {
        val count = Config.getInstance().mColumnsCount
        val divSize = SizeUtil.dp2px(context, 4f)
        val imageSize = (SizeUtil.getScreenWidth(context as Activity) - divSize * count) / count

        mRecyclerView.layoutManager = GridLayoutManager(context, count)
        mRecyclerView.addItemDecoration(
            GridItemDecoration(
                Config.getInstance().mColumnsCount,
                divSize,
                divSize,
                false
            )
        )

        mPhotoAdapter = PhotoAdapter(context, imageSize, ArrayList())
        mRecyclerView.adapter = mPhotoAdapter
    }

    fun setData(list: ArrayList<PhotoEntity>) {
        mPhotoAdapter.setData(list)
    }

}