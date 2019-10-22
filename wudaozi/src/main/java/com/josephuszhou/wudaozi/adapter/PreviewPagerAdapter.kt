package com.josephuszhou.wudaozi.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.view.PreviewFragment

/**
 * @author senfeng.zhou
 * @date 2019-10-21
 * @desc
 */
class PreviewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mPhotoList = ArrayList<PhotoEntity>()

    override fun getItem(position: Int): Fragment {
        return PreviewFragment.newInstance(mPhotoList[position])
    }

    override fun getCount(): Int {
        return mPhotoList.size
    }

    fun getAdapterItem(position: Int): PhotoEntity {
        return mPhotoList[position]
    }

    fun setData(photoList: ArrayList<PhotoEntity>) {
        mPhotoList = photoList
        notifyDataSetChanged()
    }

}