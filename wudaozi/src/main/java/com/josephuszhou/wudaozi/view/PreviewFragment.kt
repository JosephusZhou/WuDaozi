package com.josephuszhou.wudaozi.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.config.Config
import com.josephuszhou.wudaozi.entity.PhotoEntity
import com.josephuszhou.wudaozi.util.SizeUtil
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase

/**
 * @author senfeng.zhou
 * @date 2019-10-21
 * @desc
 */
class PreviewFragment: Fragment() {

    companion object {
        private const val ARGS = "args"

        fun newInstance(photoEntity: PhotoEntity): PreviewFragment {
            val fragment = PreviewFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARGS, photoEntity)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mIvTouch: ImageViewTouch? = null

    private var mSingleTapListener: ImageViewTouch.OnImageViewTouchSingleTapListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoEntity = arguments?.getParcelable<PhotoEntity>(ARGS) ?: return

        mIvTouch = view.findViewById(R.id.iv_touch)
        mIvTouch?.let {
            it.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
            it.setSingleTapListener(mSingleTapListener)

            Config.getInstance().mImageLoader.loadImage(
                context,
                SizeUtil.getScreenWidth(requireActivity()),
                SizeUtil.getScreenHeight(requireActivity()),
                it,
                photoEntity.uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ImageViewTouch.OnImageViewTouchSingleTapListener) {
            mSingleTapListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mSingleTapListener = null
    }

    fun resetView() {
        mIvTouch?.resetMatrix()
    }

}