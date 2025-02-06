package com.bharadwaj.imagevideopicker.pojo

import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import java.io.Serializable
import java.util.Comparator

data class GalleryModel(
    var bitmap: Bitmap? = null,
    var filePath: String = "",
    var itemUrI: Uri? = null,
    var videoDuration: Long = 0,
    var videoDurationFormatted: String = "00:00",
) : Serializable {
    var player: MediaPlayer? = null

}