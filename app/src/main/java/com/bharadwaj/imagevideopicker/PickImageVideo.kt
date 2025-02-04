package com.bharadwaj.imagevideopicker

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bharadwaj.imagevideopicker.listener.OnResultCallback
import com.bharadwaj.imagevideopicker.pojo.GalleryModel
import com.google.gson.Gson

class PickImageVideo(var myActivity: AppCompatActivity) {
    lateinit var pickerListener: PickerListener
    lateinit var myOnResultCallback: OnResultCallback

    fun showGallery(
        pickerType: String = PickerType.BottomSheet.name,
        pickerMode: String = PickerMode.IMAGE.name,
        onResultCallback: OnResultCallback?,
        pickerListener: PickerListener
    ) {
        this.pickerListener = pickerListener
        myOnResultCallback = onResultCallback!!

        val pickerLauncher = myActivity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                val intent = result.data
                if (intent != null) {
                    val galleryModelObj = intent.getStringExtra(GalleryModel::class.java.name)
                    if (!TextUtils.isEmpty(galleryModelObj)) {
                        val galleryObj = Gson().fromJson(galleryModelObj, GalleryModel::class.java)
                        pickerListener.onPickerResult(galleryObj)
                    }
                }
            }
        }
        PickerActivity.launchPicker(
            myActivity,
            pickerType, pickerMode, pickerLauncher
        )
    }
}