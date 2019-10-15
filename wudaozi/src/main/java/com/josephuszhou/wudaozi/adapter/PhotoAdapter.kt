package com.josephuszhou.wudaozi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.widget.CheckView
import com.josephuszhou.wudaozi.widget.PhotoGrid

class PhotoAdapter(private var mList: ArrayList<PhotoEntity>) :
    RecyclerView.Adapter<PhotoAdapter.Companion.VH>(), PhotoGrid.OnPhotoGridClickListener {

    private val mSelectedData = Config.getInstance().mSelectedData

    companion object {
        class VH(v: View) : RecyclerView.ViewHolder(v) {
            val mPhotoGrid: PhotoGrid = v as PhotoGrid
        }
    }

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
                grid.setCheckNum(Config.UNCHECKED_NUM)
            } else {
                grid.setCheckEnabled(true)
                grid.setCheckNum(Config.UNCHECKED_NUM)
            }
        }
    }

    override fun onThumbnailClick(thumbnail: AppCompatImageView, photoEntity: PhotoEntity) {

    }

    override fun onCheckViewClick(checkView: CheckView, photoEntity: PhotoEntity) {
        val checkNum = mSelectedData.checkSelectedItem(photoEntity)
        if (checkNum > 0) {
            mSelectedData.removeSelectedItem(photoEntity)
            notifyDataSetChanged()
        } else {
            if (!mSelectedData.maxSelected()) {
                mSelectedData.addSelectedItem(photoEntity)
                notifyDataSetChanged()
            }
        }
    }


}