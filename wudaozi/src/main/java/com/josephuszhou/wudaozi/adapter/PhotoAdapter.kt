package com.josephuszhou.wudaozi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.widget.PhotoGrid

class PhotoAdapter(private var mList: ArrayList<PhotoEntity>): RecyclerView.Adapter<PhotoAdapter.Companion.VH>() {

    companion object {
        class VH(v: View): RecyclerView.ViewHolder(v) {
            val mPhotoGrid: PhotoGrid = v as PhotoGrid
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
        holder.mPhotoGrid.setData(mList[position])
    }
}