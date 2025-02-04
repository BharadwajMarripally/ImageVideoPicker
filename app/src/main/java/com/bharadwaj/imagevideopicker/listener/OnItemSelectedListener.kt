package com.bharadwaj.imagevideopicker.listener

import android.widget.ImageView
import com.bharadwaj.imagevideopicker.pojo.GalleryModel

interface OnItemSelectedListener {
    fun onItemSelectedListener(position: Int)
    fun onImageView(position: Int,imageView: ImageView, galleryModels: ArrayList<GalleryModel>)
}