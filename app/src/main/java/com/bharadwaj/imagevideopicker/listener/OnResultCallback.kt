package com.bharadwaj.imagevideopicker.listener

import com.bharadwaj.imagevideopicker.pojo.GalleryModel


interface OnResultCallback {
    fun onResult(list: ArrayList<GalleryModel>)
    fun onDismiss()
}