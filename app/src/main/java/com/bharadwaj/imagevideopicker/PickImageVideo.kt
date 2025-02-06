package com.bharadwaj.imagevideopicker

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bharadwaj.imagevideopicker.listener.InterfaceHolder
import com.bharadwaj.imagevideopicker.listener.OnResultCallback
import com.bharadwaj.imagevideopicker.pojo.GalleryModel
import com.google.gson.Gson

class PickImageVideo(var myActivity: AppCompatActivity) {
    lateinit var pickerListener: PickerListener
    fun showGallery(
        pickerType: String = PickerType.BottomSheet.name,
        pickerMode: String = PickerMode.IMAGE.name,
        onResultCallback: OnResultCallback?,
        pickerListener: PickerListener
    ) {
        this.pickerListener = pickerListener
        InterfaceHolder.onResultCallback = onResultCallback
        PickerActivity.launchPicker(
            myActivity,
            pickerType, pickerMode
        )
    }
}