package com.josephuszhou.wudaozi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.AttrUtil

class PhotoAdapter(private val mContext: Context, private val size: Int, private var mList: ArrayList<PhotoEntity>): RecyclerView.Adapter<PhotoAdapter.Companion.VH>() {

    private val thumbnailPlaceHolder = AttrUtil.getDrawable(mContext, R.attr.thumbnail_placeholder)

    companion object {
        class VH(v: View): RecyclerView.ViewHolder(v) {
            val ivPhoto: AppCompatImageView = v.findViewById(R.id.iv_photo)
        }
    }

    fun setData(list: ArrayList<PhotoEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        Config.getInstance().mImageLoader.loadThumbnail(mContext, size, thumbnailPlaceHolder, holder.ivPhoto, mList[position].uri)
    }
}