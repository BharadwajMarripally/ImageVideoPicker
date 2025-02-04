package com.bharadwaj.imagevideopicker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bharadwaj.imagevideopicker.constants.Keys
import com.bharadwaj.imagevideopicker.screendesigns.BottomSheetTypePicker
import com.bharadwaj.imagevideopicker.screendesigns.DialogTypePicker
import com.bharadwaj.imagevideopicker.ui.theme.ImageVideoPickerTheme
import com.bharadwaj.imagevideopicker.utils.Globals

class PickerActivity : ComponentActivity() {

    private var pickerType = ""
    private var pickerMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickerType = intent?.getStringExtra(Keys.PICKER_TYPE) ?: PickerType.BottomSheet.name
        pickerMode = intent?.getStringExtra(Keys.PICKER_MODE) ?: PickerMode.IMAGE.name

        setContent {
            ImageVideoPickerTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                ) { innerPadding ->
                    if (pickerType == PickerType.BottomSheet.name) {
                        BottomSheetTypePicker(this@PickerActivity)
                    } else if (pickerType == PickerType.Dialog.name) {
                        DialogTypePicker()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val galleryStringObj = Globals.loadPreference(this@PickerActivity, Keys.GALLERY_OBJECT)
        Globals.log(this@PickerActivity, galleryStringObj)
        Globals.customToast(this@PickerActivity, "Activity Destroyed", false)
    }

    companion object {
        fun launchPicker(
            context: Context,
            pickerType: String = PickerType.BottomSheet.name,
            pickerMode: String = PickerMode.IMAGE.name,
            pickerLauncher: ActivityResultLauncher<Intent>?
        ) {
            context.startActivity(
                Intent(
                    context, PickerActivity::class.java
                )
                    .putExtra(Keys.PICKER_TYPE, pickerType)
                    .putExtra(Keys.PICKER_MODE, pickerMode)
            )
        }
    }

}