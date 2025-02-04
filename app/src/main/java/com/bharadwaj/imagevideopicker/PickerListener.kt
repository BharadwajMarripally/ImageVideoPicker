package com.bharadwaj.imagevideopicker

import com.bharadwaj.imagevideopicker.pojo.GalleryModel
import java.io.File

interface PickerListener {
    
    fun onPickerResult(galleryModel: GalleryModel)
}