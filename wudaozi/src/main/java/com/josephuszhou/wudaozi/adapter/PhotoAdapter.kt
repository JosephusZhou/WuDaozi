package com.josephuszhou.wudaozi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.data.SelectedData
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.widget.CheckView
import com.josephuszhou.wudaozi.widget.PhotoGrid

class PhotoAdapter(private var mList: ArrayList<PhotoEntity>) :
    RecyclerView.Adapter<PhotoAdapter.Companion.VH>(), PhotoGrid.OnPhotoGridClickListener {

    companion object {
        class VH(v: View) : RecyclerView.ViewHolder(v) {
            val mPhotoGrid: PhotoGrid = v as PhotoGrid
        }
    }

    private val mSelectedData = SelectedData.getInstance()

    private var mOnCheckStateListener: OnCheckStateListener? = null

    private var mOnThumbnailClickListener: OnThumbnailClickListener? = null

    fun setOnCheckStateListener(onCheckStateListener: OnCheckStateListener) {
        mOnCheckStateListener = onCheckStateListener
    }

    fun setOnThumbnailClickListener(onThumbnailClickListener: OnThumbnailClickListener) {
        mOnThumbnailClickListener = onThumbnailClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<PhotoEntity>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.mPhotoGrid.setData(mList[position])
        holder.mPhotoGrid.setOnPhotoGridClickListener(this)
        setCheckStatus(mList[position], holder.mPhotoGrid)
    }

    private fun setCheckStatus(entity: PhotoEntity, grid: PhotoGrid) {
        val checkNum = mSelectedData.checkSelectedItem(entity)
        if (checkNum > 0) {
            grid.setCheckEnabled(true)
            grid.setCheckNum(checkNum)
        } else {
            if (mSelectedData.maxSelected()) {
                grid.setCheckEnabled(false)
                grid.setCheckNum(CheckView.UNCHECKED_NUM)
            } else {
                grid.setCheckEnabled(true)
                grid.setCheckNum(CheckView.UNCHECKED_NUM)
            }
        }
    }

    override fun onThumbnailClick(thumbnail: AppCompatImageView, photoEntity: PhotoEntity) {
        mOnThumbnailClickListener?.onThumbnailClick(photoEntity)
    }

    override fun onCheckViewClick(checkView: CheckView, photoEntity: PhotoEntity) {
        val checkNum = mSelectedData.checkSelectedItem(photoEntity)
        if (checkNum > 0) {
            mSelectedData.removeSelectedItem(photoEntity)
            notifyChanged()
        } else {
            if (!mSelectedData.maxSelected() && mSelectedData.isAcceptable(checkView.context, photoEntity)) {
                mSelectedData.addSelectedItem(photoEntity)
                notifyChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyChanged() {
        notifyDataSetChanged()
        mOnCheckStateListener?.onCheckStateChanged()
    }

    interface OnCheckStateListener {
        fun onCheckStateChanged()
    }

    interface OnThumbnailClickListener {
        fun onThumbnailClick(photoEntity: PhotoEntity)
    }

}