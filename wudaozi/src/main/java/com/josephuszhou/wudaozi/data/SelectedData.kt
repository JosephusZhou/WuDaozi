package com.josephuszhou.wudaozi.data

import android.content.Context
import android.net.Uri
import android.util.SparseArray
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity

/**
 * @author senfeng.zhou
 * @date 2019-10-15
 * @desc
 */
class SelectedData private constructor() {

    companion object {
        fun getInstance() = InstanceHolder.INSTANCE

        fun getInitialInstance(): SelectedData {
            val instance = getInstance()
            instance.reset()
            return instance
        }
    }

    private object InstanceHolder {
        val INSTANCE = SelectedData()
    }

    private var mSelectedList = ArrayList<PhotoEntity>()

    private var mSelectedMap = SparseArray<Long>()

    fun reset() {
        mSelectedList = ArrayList()
        mSelectedMap = SparseArray()
    }

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

    fun selectedCount(): Int {
        return mSelectedList.size
    }

    fun getUriList(): ArrayList<Uri> {
        val uriList = ArrayList<Uri>()
        for(entity in mSelectedList) {
            uriList.add(entity.uri)
        }
        return uriList
    }

    fun isAcceptable(context: Context, entity: PhotoEntity): Boolean {
        Config.getInstance().mFilter?.let {
            return it.filter(context, entity)
        }
        return true
    }

}