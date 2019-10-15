package com.josephuszhou.wudaozi.widget

import android.util.SparseArray
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity

/**
 * @author senfeng.zhou
 * @date 2019-10-15
 * @desc
 */
class SelectedData {

    private var mSelectedList = ArrayList<PhotoEntity>()

    private var mSelectedMap = SparseArray<Long>()

    fun addSelectedItem(entity: PhotoEntity) {
        mSelectedList.add(entity)
        mSelectedMap.put(mSelectedList.size - 1, entity.id)
    }

    fun removeSelectedItem(entity: PhotoEntity) {
        for(i in mSelectedList.indices) {
            if (mSelectedList[i].id == entity.id) {
                mSelectedMap.remove(i)
                mSelectedList.removeAt(i)
                break
            }
        }
    }

    fun checkSelectedItem(entity: PhotoEntity): Int {
        for(i in mSelectedList.indices) {
            if (mSelectedList[i].id == entity.id) {
                return i + 1
            }
        }
        return Int.MIN_VALUE
    }

    fun maxSelected(): Boolean {
        return mSelectedList.size == Config.getInstance().mMaxSelectableCount
    }

}