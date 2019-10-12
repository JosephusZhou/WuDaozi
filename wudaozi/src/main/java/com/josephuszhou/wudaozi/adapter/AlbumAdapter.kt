package com.josephuszhou.wudaozi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.AlbumEntity

class AlbumAdapter(private var context: Context, private var mList: ArrayList<AlbumEntity>) :
    BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder = if (convertView == null) {
            ViewHolder(context)
        } else {
            convertView.tag as ViewHolder
        }
        viewHolder.setData(mList[position])
        return viewHolder.contentView
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }

    companion object {
        class ViewHolder(private val context: Context) {
            @SuppressLint("InflateParams")
            var contentView: View =
                LayoutInflater.from(context).inflate(R.layout.layout_album_item, null)
            private var ivAlbumThumbnail: ImageView
            private var tvAlbumName: TextView
            private var tvAlbumPhotoCount: TextView

            private var albumEntity: AlbumEntity? = null

            init {
                ivAlbumThumbnail = contentView.findViewById(R.id.iv_album_thumbnail)
                tvAlbumName = contentView.findViewById(R.id.tv_album_name)
                tvAlbumPhotoCount = contentView.findViewById(R.id.tv_album_photo_count)
                contentView.tag = this
            }

            fun setData(albumEntity: AlbumEntity) {
                this.albumEntity = albumEntity
                updateView()
            }

            private fun updateView() {
                albumEntity?.let {
                    Config.getInstance().mImageLoader.loadThumbnail(
                        context,
                        context.resources.getDimensionPixelSize(R.dimen.wudaozi_album_thumbnail_size),
                        null,
                        ivAlbumThumbnail,
                        it.thumbnail.uri
                    )
                    tvAlbumName.text = it.bucketName
                    tvAlbumPhotoCount.text = it.photoCount.toString()
                }
            }

        }
    }

}